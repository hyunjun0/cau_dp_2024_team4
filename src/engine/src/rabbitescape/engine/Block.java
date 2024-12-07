package rabbitescape.engine;

import static rabbitescape.engine.Direction.*;
import rabbitescape.engine.util.LookupItem2D;
import rabbitescape.engine.util.Position;

public class Block implements LookupItem2D
{
    public enum Material
    {
        EARTH,
        METAL
    }

    public enum Shape
    {
        FLAT,
        UP_RIGHT,
        UP_LEFT,
        BRIDGE_UP_RIGHT,
        BRIDGE_UP_LEFT
    }

    public final int x;
    public final int y;
    public final Material material;
    public final Shape shape;
    public final int variant;

    private Block(BuilderBlock builder)
    {
        this.x = builder.x;
        this.y = builder.y;
        this.material = builder.material;
        this.shape = builder.shape;
        this.variant = builder.variant;
    }
    
    public static class BuilderBlock {
    	private int x;
    	private int y;
    	private Material material;
    	private Shape shape;
    	private int variant = 0;
    	
    	public BuilderBlock setPosition(int x, int y) {
    	    this.x = x;
    	    this.y = y;
    	    return this;
    	}
    	
    	public BuilderBlock setMaterial(Material material) {
    	    this.material = material;
    	    return this;
    	}
    	
    	public BuilderBlock setShape(Shape shape) {
    	    this.shape = shape;
    	    return this;
    	}
    	
    	public BuilderBlock setVariant(int variant) {
    	    this.variant = variant;
    	    return this;
    	}
    	
    	public Block build() {
    	    if (shape == null || material == null) {
    	        throw new RuntimeException( "Unknown block create " + shape );
    	}
    	return new Block(this);
    	}
    }

    public Direction riseDir()
    {
        switch ( shape )
        {
        case FLAT:
            return DOWN;

        case UP_RIGHT:
        case BRIDGE_UP_RIGHT:
            return RIGHT;

        case UP_LEFT:
        case BRIDGE_UP_LEFT:
            return LEFT;

        default:
            throw new RuntimeException( "Unknown block shape " + shape );
        }
    }

    public boolean isBridge()
    {
        switch ( shape )
        {
            case BRIDGE_UP_LEFT:
            case BRIDGE_UP_RIGHT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public Position getPosition()
    {
        return new Position( x, y );
    }
}
