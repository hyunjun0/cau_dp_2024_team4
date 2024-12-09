package rabbitescape.engine;

import static rabbitescape.engine.ChangeDescription.State.*;

import rabbitescape.engine.Token.UnknownType;
import rabbitescape.engine.ChangeDescription.State;

public class TokenStateFactory {
    

    private static State chooseState( 
        boolean moving, 
        boolean slopeBelow,
        boolean onSlope, 
        State falling,
        State onFlat, 
        State fallingToSlope,
        State onSlopeState
    )
    {
        if ( onSlope )
        {
            return onSlopeState;
        }
        if ( !moving )
        {
            return onFlat;
        }
        if ( slopeBelow )
        {
            return fallingToSlope;
        }
        return falling;
    }
    
    public static State switchType( 
        Token.Type type, 
        boolean moving,
        boolean slopeBelow, 
        boolean onSlope 
    )
    {
        switch( type )
        {
            case bash:   return chooseState( 
                moving, 
                slopeBelow, 
                onSlope,
                TOKEN_BASH_FALLING, 
                TOKEN_BASH_STILL,
                TOKEN_BASH_FALL_TO_SLOPE, 
                TOKEN_BASH_ON_SLOPE
                );

            case dig:    return chooseState( 
                moving, 
                slopeBelow, 
                onSlope,
                TOKEN_DIG_FALLING, 
                TOKEN_DIG_STILL,
                TOKEN_DIG_FALL_TO_SLOPE, 
                TOKEN_DIG_ON_SLOPE
                );

            case bridge: return chooseState( 
                moving, 
                slopeBelow, 
                onSlope,
                TOKEN_BRIDGE_FALLING, 
                TOKEN_BRIDGE_STILL,
                TOKEN_BRIDGE_FALL_TO_SLOPE, 
                TOKEN_BRIDGE_ON_SLOPE
                );

            case block: return chooseState( 
                moving, 
                slopeBelow, 
                onSlope,
                TOKEN_BLOCK_FALLING, 
                TOKEN_BLOCK_STILL,
                TOKEN_BLOCK_FALL_TO_SLOPE, 
                TOKEN_BLOCK_ON_SLOPE
                );

            case climb: return chooseState( 
                moving, 
                slopeBelow, 
                onSlope,
                TOKEN_CLIMB_FALLING, 
                TOKEN_CLIMB_STILL,
                TOKEN_CLIMB_FALL_TO_SLOPE, 
                TOKEN_CLIMB_ON_SLOPE
                );

            case explode: return chooseState( 
                moving, 
                slopeBelow, 
                onSlope,
                TOKEN_EXPLODE_FALLING, 
                TOKEN_EXPLODE_STILL,
                TOKEN_EXPLODE_FALL_TO_SLOPE, 
                TOKEN_EXPLODE_ON_SLOPE)
                ;

            case brolly: return chooseState( 
                moving, 
                slopeBelow, 
                onSlope,
                TOKEN_BROLLY_FALLING, 
                TOKEN_BROLLY_STILL,
                TOKEN_BROLLY_FALL_TO_SLOPE, 
                TOKEN_BROLLY_ON_SLOPE
                );

            default: throw new UnknownType( type );
        }
    }
}
