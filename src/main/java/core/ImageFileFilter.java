/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import java.io.File;
import java.io.FileFilter;

/**
 * return only images
 * @author Bene
 */
public class ImageFileFilter implements FileFilter
{
	private final String[] okFileExtensions = new String[]{"png","jpg","gif","jpeg"};

	@Override
	public boolean accept(File file)
	{
		for (String extension : okFileExtensions)
		{
			return file.getName().toLowerCase().endsWith(extension);
		}
		return false;
	}
}
