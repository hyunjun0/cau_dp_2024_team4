package rabbitescape.engine;

import java.util.Map;

import rabbitescape.engine.ChangeDescription.State;

public interface Behaviour
{
    

    boolean triggered = false;

    /**
     * Subclasses examine the rabbit's situation using BehaviourTools and
     * return the state (see ChangeDescription) for the next time step.
     * This method may return null indicating that a different Behaviour
     * must take over.
     *
     * Note that the state determines the animation used.
     */
    State newState(BehaviourTools t, boolean triggered);

    /**
     * Move the rabbit in the world. Kill it, or record its safe exit.
     *
     * @return true if this behaviour has done all the work needed for
     *         this time step
     */
    boolean behave(World world, Rabbit rabbit, State state);

    /**
     * Examine the rabbit's situation and return true if this Behaviour must
     * take control.
     */
    boolean checkTriggered(Rabbit rabbit, World world);

    void cancel();

    default void saveState(Map<String, String> saveState) { }

    default void restoreFromState(Map<String, String> saveState) { }
}
