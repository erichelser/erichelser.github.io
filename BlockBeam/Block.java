import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;

class Block
{
	//CONSTANT SYMBOLS FOR BLOCK TYPES
	public static final BlockType BLOCK_UNDEFINED=new BlockType(Color.BLUE);
	public static final BlockType BLOCK_EMPTY    =new BlockType("img/empty.jpg");
	public static final BlockType BLOCK_WALL     =new BlockType("img/wall.jpg");


	public static final BlockType BLOCK_NORMAL   =new BlockType("img/bluegem1.jpg",true);
	public static final BlockType BLOCK_DIAGONAL =new BlockType("img/bluegem2.jpg",true);
	public static final BlockType BLOCK_ALLWAY   =new BlockType("img/bluegem3.jpg",true);

	public static final BlockType BLOCK_GREEN   =new BlockType("img/greengem.jpg",true);

	public static final BlockType BLOCK_NORMAL_CHAIN   =new BlockType("img/hazard1.jpg",true);


	public static final BlockType BLOCK_HAZARD   =new BlockType("img/redgem1.jpg");
	public static final BlockType BLOCK_HAZARD_X =new BlockType("img/redgem2.jpg");


	public static final BlockType BLOCK_MIRROR_NE=new BlockType("img/mirror_ne.jpg");
	public static final BlockType BLOCK_MIRROR_NW=new BlockType("img/mirror_nw.jpg");
	public static final BlockType BLOCK_MIRROR_SE=new BlockType("img/mirror_se.jpg");
	public static final BlockType BLOCK_MIRROR_SW=new BlockType("img/mirror_sw.jpg");
	public static final BlockType BLOCK_MIRROR_N =new BlockType("img/mirror_n.jpg");
	public static final BlockType BLOCK_MIRROR_W =new BlockType("img/mirror_w.jpg");
	public static final BlockType BLOCK_MIRROR_S =new BlockType("img/mirror_s.jpg");
	public static final BlockType BLOCK_MIRROR_E =new BlockType("img/mirror_e.jpg");

	//THIS BLOCK'S TYPE
	private BlockType blockType;

	public Block() { blockType=BLOCK_UNDEFINED; }
	public Block(BlockType type) { this(); setBlockType(type); }

	public void setBlockType(BlockType type) { blockType=type; }

	//SHORTCUT METHODS FOR SETTING THE BLOCK TYPE
	public void setEmpty()  { setBlockType(BLOCK_EMPTY);  }
	public void setNormal() { setBlockType(BLOCK_NORMAL); }
	public void setHazard() { setBlockType(BLOCK_HAZARD); }

	public BlockType getBlockType() { return blockType; }
	public Image getImage() { return blockType.getImage(); }

	//used to init a board, shorthand notation basically
	public static BlockType interpretSymbol(char c)
	{
		if     (c==' ' || c=='.') return BLOCK_EMPTY;

		else if(c=='#') return BLOCK_HAZARD;
		else if(c=='O') return BLOCK_WALL;

		else if(c=='Z') return BLOCK_NORMAL;
		else if(c=='X') return BLOCK_DIAGONAL;
		else if(c=='C') return BLOCK_ALLWAY;
		else if(c=='V') return BLOCK_GREEN;

		else if(c=='!') return BLOCK_NORMAL_CHAIN;

		else if(c=='7') return BLOCK_MIRROR_NW;
		else if(c=='8') return BLOCK_MIRROR_N;
		else if(c=='9') return BLOCK_MIRROR_NE;
		else if(c=='4') return BLOCK_MIRROR_W;
		else if(c=='6') return BLOCK_MIRROR_E;
		else if(c=='1') return BLOCK_MIRROR_SW;
		else if(c=='2') return BLOCK_MIRROR_S;
		else if(c=='3') return BLOCK_MIRROR_SE;

		return BLOCK_UNDEFINED;
	}
}
