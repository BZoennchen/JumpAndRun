/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import java.io.File;
import java.io.FileFilter;

/**
 * return only directories
 * @author Benedikt Zönnchen
 */
public class DirFileFilter implements FileFilter
{
	@Override
	public boolean accept(File file)
	{
		return file.isDirectory();
	}

}
