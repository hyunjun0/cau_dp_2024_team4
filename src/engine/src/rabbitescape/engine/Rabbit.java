package rabbitescape.engine;

import static rabbitescape.engine.ChangeDescription.State.*;

import java.util.HashMap;
import java.util.Map;

import rabbitescape.engine.ChangeDescription.State;

public class Rabbit extends Thing implements Comparable<Rabbit>
{
    public static enum Type
    {
        RABBIT,
        RABBOT
    }

    public final static int NOT_INDEXED = 0;
    private final RabbitBehaviourManager behaviourManager;

    public int index;

    public Direction dir;
    public boolean onSlope;
    /** Rabbits move up 1 cell to bash from a slope.
     *  Keep a note, so it can be undone.  */
    public boolean slopeBashHop = false;
    public final Type type;

    public Rabbit( int x, int y, Direction dir, Type type )
    {
        super( x, y, RABBIT_WALKING_LEFT );
        this.dir = dir;
        this.onSlope = false;
        this.type = type;
        index = NOT_INDEXED;
        behaviourManager = new RabbitBehaviourManager(getFatalHeight());
    }

    public boolean isFallingToDeath()
    {
        return behaviourManager.falling.isFallingToDeath();
    }

    @Override
    public void calcNewState( World world )
    {
        for ( Behaviour behaviour : behaviourManager.behavioursTriggerOrder )
        {
            behaviour.triggered = false;
        }

        for ( Behaviour behaviour : behaviourManager.behavioursTriggerOrder )
        {
            behaviour.triggered = behaviour.checkTriggered( this, world );
            if ( behaviour.triggered )
            {
                cancelAllBehavioursExcept( behaviour );
            }
        }

        boolean done = false;
        for ( Behaviour behaviour : behaviourManager.behaviours )
        {

            State thisState = behaviour.newState(
                new BehaviourTools( this, world ), behaviour.triggered );

            if ( thisState != null && !done )
            {
                state = thisState;
                done = true;
            }
        }

    }

    private void cancelAllBehavioursExcept( Behaviour exception )
    {
        for ( Behaviour behaviour : behaviourManager.behaviours )
        {
            if ( behaviour != exception )
            {
                behaviour.cancel();
            }
        }
    }

    public void possiblyUndoSlopeBashHop( World world )
    {
        if ( !slopeBashHop )
        {
            return;
        }
        BehaviourTools t = new BehaviourTools( this, world );
        if ( t.blockHere() != null ||
            !BehaviourTools.isSlope( t.blockBelow() ) )
        {
            return;
        }
        ++y;
        slopeBashHop = false;
    }

    @Override
    public void step( World world )
    {
        for ( Behaviour behaviour : behaviourManager.behaviours )
        {
            boolean handled = behaviour.behave( world, this, state );
            if ( handled )
            {
                break;
            }
        }
    }

    @Override
    public Map<String, String> saveState( boolean runtimeMeta )
    {
        Map<String, String> ret = new HashMap<String, String>();
        if ( !runtimeMeta )
        {
            return ret;
        }

        BehaviourState.addToStateIfGtZero( ret, "index", index );
        BehaviourState.addToStateIfTrue( ret, "onSlope", onSlope );

        for ( Behaviour behaviour : behaviourManager.behaviours )
        {
            behaviour.saveState( ret );
        }

        return ret;
    }

    @Override
    public void restoreFromState( Map<String, String> state )
    {
        index = BehaviourState.restoreFromState( state, "index", -1 );

        onSlope = BehaviourState.restoreFromState(
            state, "onSlope", false
        );

        for ( Behaviour behaviour : behaviourManager.behaviours )
        {
            behaviour.restoreFromState( state );
        }
    }

    @Override
    public String overlayText()
    {
        String fmt;
        switch ( dir )
        {
        case LEFT:
            fmt = "<[%d] ";
            break;
        case RIGHT:
            fmt = " [%d]>";
            break;
        default:
            throw new RuntimeException( "Rabbit should only be left or right");
        }
        return String.format( fmt, index ) ;
    }

    @Override
    public int compareTo( Rabbit r )
    {
        return this.index - r.index;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( null == o || !( o instanceof Rabbit ) )
        {
            return false;
        }
        return ( (Rabbit)o ).index == this.index;
    }

    @Override
    public int hashCode()
    {
        return index;
    }

    @Override
    public String stateName()
    {
        String normalName = super.stateName();
        if ( type == Type.RABBIT )
        {
            return normalName;
        }
        else
        {
            return normalName.replaceFirst(
                "^rabbit", type.name().toLowerCase() );
        }
    }

    /** Rabbots can fall further than rabbits. */
    private int getFatalHeight()
    {
        return ( type == Type.RABBIT ? 4 : 5 );
    }
}
