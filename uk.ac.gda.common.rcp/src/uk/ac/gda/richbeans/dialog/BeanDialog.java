/*-
 * Copyright © 2011 Diamond Light Source Ltd.
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

package uk.ac.gda.richbeans.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.gda.richbeans.beans.BeanUI;

/**
 * This dialog can be used for UI to edit a bean. Inherit from it and
 * override the method:
 * 
 * 	protected Composite createDialogArea(Composite ancestor) {
 
    }
    
    Then simply expose the bean fields with getters of the same name in this dialog as usual.
    
    The dialog is then uses as follows:
    <code>
        MyDialog dialog = new MyDialog(getSite().getShell()); // extends BeanDialog
		dialog.create();
		dialog.getShell().setSize(400,435); // As needed
		
		dialog.setBean(bean);
        final int ok = dialog.open();
        
        if (ok == Dialog.OK) {
            bean = dialog.getBean();
            // etc
        }
    </code>
 * 
 * 
 */
public abstract class BeanDialog extends Dialog {
	
	private static final Logger logger = LoggerFactory.getLogger(BeanDialog.class);
	
	protected Object bean;
	

	protected BeanDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.RESIZE|SWT.TITLE);
	}
	
	@Override
	public void create() {
		super.create();
		getShell().setText("Configure User Input");
	}

	@Override
	public boolean close() {	
		if (getReturnCode()==OK) {
			try {
				BeanUI.switchState(bean, this, false);
				BeanUI.uiToBean(this, bean);			
			} catch (Exception e) {
				logger.error("Cannot get "+bean+" from dialog!", e);
			}	
		}
		return super.close();
	}
	
	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		try {
			this.bean = bean;
			BeanUI.beanToUI(bean, this);
			BeanUI.switchState(bean, this, true);
			BeanUI.fireValueListeners(bean, this);
		} catch (Exception e) {
			logger.error("Cannot send "+bean+" to dialog!", e);
		}		
	}

}
