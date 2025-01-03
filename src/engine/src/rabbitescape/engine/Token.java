package rabbitescape.engine;


import java.util.HashMap;
import java.util.Map;

import rabbitescape.engine.err.RabbitEscapeException;

public class Token extends Thing
{
    public static class UnknownType extends RabbitEscapeException
    {
        public final Type type;

        public UnknownType( Type type )
        {
            this.type = type;
        }

        private static final long serialVersionUID = 1L;
    }

    public static enum Type
    {
        bash,
        dig,
        bridge,
        block,
        climb,
        explode,
        brolly
    }

    public final Type type;

    public Token( int x, int y, Type type )
    {
        super( x, y, TokenStateFactory.switchType( type, false, false, true ) );
        this.type = type;
    }

    public Token( int x, int y, Type type, World world )
    {
        this( x, y, type );
        boolean onSlope = BehaviourTools.isSlope( world.getBlockAt( x, y ) );
        // Can't use calcNewState here since we have just been created, so
        // can't be moving (until a time step passes).
        state = TokenStateFactory.switchType( type, false, false, onSlope );
    }

    @Override
    public void calcNewState( World world )
    {
        Block onBlock = world.getBlockAt( x, y );
        Block belowBlock = world.getBlockAt( x, y + 1 );
        boolean still = (
               BehaviourTools.s_isFlat( belowBlock )
            || ( onBlock != null )
            || BridgeTools.someoneIsBridgingAt( world, x, y )
        );

        state = TokenStateFactory.switchType( type, !still,
            BehaviourTools.isSlope( belowBlock ),
            BehaviourTools.isSlope( onBlock ) );
    }

    @Override
    public void step( World world )
    {
        switch ( state )
        {
        case TOKEN_BASH_FALLING:
        case TOKEN_BASH_FALL_TO_SLOPE:
        case TOKEN_DIG_FALLING:
        case TOKEN_DIG_FALL_TO_SLOPE:
        case TOKEN_BRIDGE_FALLING:
        case TOKEN_BRIDGE_FALL_TO_SLOPE:
        case TOKEN_BLOCK_FALLING:
        case TOKEN_BLOCK_FALL_TO_SLOPE:
        case TOKEN_CLIMB_FALLING:
        case TOKEN_CLIMB_FALL_TO_SLOPE:
        case TOKEN_EXPLODE_FALL_TO_SLOPE:
        case TOKEN_EXPLODE_FALLING:
        case TOKEN_BROLLY_FALLING:
        case TOKEN_BROLLY_FALL_TO_SLOPE:
        {
            ++y;

            if ( y >= world.size.height )
            {
                world.changes.removeToken( this );
            }

            return;
        }
        default:
            // Nothing to do
        }
    }

    @Override
    public Map<String, String> saveState( boolean runtimeMeta )
    {
        return new HashMap<String, String>();
    }

    @Override
    public void restoreFromState( Map<String, String> state )
    {
    }

    public static String name( Type ability )
    {
        String n = ability.name();
        return n.substring( 0, 1 ).toUpperCase() + n.substring( 1 );
    }

    @Override
    public String overlayText()
    {
        return type.toString();
    }
}
