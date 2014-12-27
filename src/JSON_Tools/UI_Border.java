package JSON_Tools;

import javax.swing.border.*;
import javax.swing.BorderFactory;
import java.awt.Color;

public class UI_Border
{	public UI_Border()
	{	// nothing to contruct - border is made on demand.
	}

	public Border getInsideBorder()	// (new UI_Border()).getInsideBorder()
	{	return (new CompoundBorder(BorderFactory.createLineBorder(Color.black), BorderFactory.createEmptyBorder(5,5,5,5)));
	}

	public Border getOutsideBorder()	// (new UI_Border()).getOutsideBorder()
	{	return (new CompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createEmptyBorder(5,5,5,5)));
	}

	public Border getProgBorder()		// 	(new UI_Border()).getProgBorder()
	{	return (new CompoundBorder(getOutsideBorder(), getInsideBorder()));
	}
}