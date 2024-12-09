package rabbitescape.engine;


import java.util.ArrayList;
import java.util.List;

import rabbitescape.engine.behaviours.*;

public class RabbitBehaviourManager {

    public final List<Behaviour> behaviours;
    public final List<Behaviour> behavioursTriggerOrder;
    public Falling falling;

    public RabbitBehaviourManager(int fataltHeight) {
        behaviours = new ArrayList<>();
        behavioursTriggerOrder = new ArrayList<>();
        createBehaviours(fataltHeight);
    }

    public void createBehaviours(int fataltHeight) {
        Climbing climbing = new Climbing();
        Digging digging = new Digging();
        Exploding exploding = new Exploding();
        Burning burning = new Burning();
        OutOfBounds outOfBounds = new OutOfBounds();
        Drowning drowning = new Drowning();
        Exiting exiting = new Exiting();
        Brollychuting brollychuting = new Brollychuting( climbing, digging );
        falling = new Falling( climbing, brollychuting, fataltHeight);
        Bashing bashing = new Bashing();
        Bridging bridging = new Bridging();
        Blocking blocking = new Blocking();
        Walking walking = new Walking();
        RabbotCrash rabbotCrash = new RabbotCrash();
        RabbotWait rabbotWait = new RabbotWait();

        behavioursTriggerOrder.add( exploding );
        behavioursTriggerOrder.add( outOfBounds );
        behavioursTriggerOrder.add( burning );
        behavioursTriggerOrder.add( drowning );
        behavioursTriggerOrder.add( rabbotCrash );
        behavioursTriggerOrder.add( falling );
        behavioursTriggerOrder.add( exiting );
        behavioursTriggerOrder.add( brollychuting );
        behavioursTriggerOrder.add( climbing );
        behavioursTriggerOrder.add( bashing );
        behavioursTriggerOrder.add( digging );
        behavioursTriggerOrder.add( bridging );
        behavioursTriggerOrder.add( blocking );
        behavioursTriggerOrder.add( rabbotWait );
        behavioursTriggerOrder.add( walking );

        behaviours.add( exploding );
        behaviours.add( outOfBounds );
        behaviours.add( burning );
        behaviours.add( drowning );
        behaviours.add( rabbotCrash );
        behaviours.add( falling );
        behaviours.add( exiting );
        behaviours.add( brollychuting );
        behaviours.add( bashing );
        behaviours.add( digging );
        behaviours.add( bridging );
        behaviours.add( blocking );
        behaviours.add( climbing );
        behaviours.add( rabbotWait );
        behaviours.add( walking );

        assert behavioursTriggerOrder.size() == behaviours.size();
    }

    public List<Behaviour> getBehaviours() {
        return behaviours;
    }
}
