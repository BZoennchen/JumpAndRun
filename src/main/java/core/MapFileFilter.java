/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author Bene
 */
public class MapFileFilter implements FileFilter
{

	private final String[] okFileExtensions = new String[]{"txt"};
	private final Pattern pattern = Pattern.compile("Map\\d+\\d?.*");

	@Override
	public boolean accept(File file)
	{
		Matcher m = pattern.matcher(file.getName());
		if(m.find())
		{
			for (String extension : okFileExtensions)
			{
				return file.getName().toLowerCase().endsWith(extension);
			}
		}
		
		return false;
	}
}
