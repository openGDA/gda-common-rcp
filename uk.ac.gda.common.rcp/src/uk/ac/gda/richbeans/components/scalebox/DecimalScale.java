/*-
 * Copyright © 2009 Diamond Light Source Ltd.
 *
 * This file is part of GDA.
 *
 * GDA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License version 3 as published by the Free
 * Software Foundation.
 *
 * GDA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along
 * with GDA. If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.gda.richbeans.components.scalebox;

import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;

/**
 * Designed to be used by ScaleBox only at the moment because
 * the order that the setters are called in matters.
 * 
 * This class is a decimal scale which also allows negative ranges.
 */
class DecimalScale {

	private Scale  scale;
	private double offset    = 0; // A scalar (>0) representing the offset which we stagger bounds by.
	private double scaleset  = 1;
	/**
	 * @param comp
	 * @param flags
	 */
	public DecimalScale(Composite comp, int flags) {
		this.scale = new Scale(comp, flags);
	}
	
	private final int toScale(double val) {
		val+=offset;  // Increase value.
		val*=scaleset;
		return (int)val;
	}
	
	private final double toWorld(double val) {
		val/=scaleset;
        val-=offset;  // Decrease value.
        return val;
	}

	/**
	 *
	 * @return sel
	 */
	public double getSelection() {
		return toWorld(scale.getSelection());
	}
	
	/**
	 *
	 * @param val 
	 * @see org.eclipse.swt.widgets.Scale#setSelection(int)
	 */
	public void setSelection(double val) {
		scale.setSelection(toScale(val));
	}
	
	private void calculateOffset() {
		// The widget can deal with positive ranges ok
		if (minimum>=0&&maximum>0) {
			this.offset=0;
		} else {
			this.offset = -1*minimum;
		}
		scale.setMinimum(toScale(minimum));
		scale.setMaximum(toScale(maximum));
	}
	
	private double minimum = 0;
	private double maximum = 100;
	/**
	 * @return Returns the minimum.
	 */
	public double getMinimum() {
		return toWorld(scale.getMinimum());
	}

	/**
	 * @param min The minimum to set.
	 */
	public void setMinimum(final double min) {
		this.minimum = min;
		if (min>=maximum) maximum = min+1;
		calculateOffset();
	}

	/**
	 * @return Returns the maximum.
	 */
	public double getMaximum() {
		return toWorld(scale.getMaximum());
	}

	/**
	 * @param max The maximum to set.
	 */
	public void setMaximum(final double max) {
		this.maximum = max;
		if (max<=minimum) minimum = max-1;
		calculateOffset();
	}

	public void setLayoutData(Object data) {
		scale.setLayoutData(data);
	}

	public void addListener(int selection, Listener listener) {
		scale.addListener(selection, listener);
	}

	public void addFocusListener(FocusListener focusListener) {
		scale.addFocusListener(focusListener);
	}
	
	/**
	 * Be careful calling this method. Large numbers of decimal places
	 * combined with large possible numbers can result in integer space being used up.
	 * @param places 
	 */
	public void setDecimalPlaces(int places) {
		this.scaleset = Math.pow(10, places);
	}

	public void setIncrement(double increment) {
		scale.setIncrement((int)(increment*scaleset));
	}

	public void setPageIncrement(double pageIncrement) {
		scale.setPageIncrement((int)(pageIncrement*scaleset));
	}
}
