/**
 *
 * Shire - Blog aware static site generator 
 * Copyright (c) 2012, Sandeep Gupta
 * 
 * http://www.sangupta/projects/shire
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.sangupta.shire.tags;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

import com.sangupta.shire.core.Tag;

public abstract class AbstractCustomTag extends Directive implements Tag {
	
	protected Node node = null;
	
	protected InternalContextAdapter context = null;
	
	protected Writer writer = null;
	
	/**
	 * Child classes should implement either this or the {@link #doTag()} method.
	 * 
	 * @see org.apache.velocity.runtime.directive.Directive#render(org.apache.velocity.context.InternalContextAdapter, java.io.Writer, org.apache.velocity.runtime.parser.node.Node)
	 */
	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
		this.context = context;
		this.node = node;
		this.writer = writer;
		
		return doTag();
	}
	
	/**
	 * Child classes should implement one of the {@link #render(InternalContextAdapter, Writer, Node)} or this method.
	 * The {@link #node}, {@link #context} and {@link #writer} objects are directly available when using this method.
	 * Also, preferable is to use the {@link #getArgument(int)} method instead of handling other parameters.
	 * 
	 * @return
	 */
	protected boolean doTag() throws IOException {
		return true;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T getArgument(int index) {
		if(this.node.jjtGetNumChildren() > index && this.node.jjtGetChild(index) != null) {
			return (T) this.node.jjtGetChild(index).value(this.context);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T getArgument(Node node, InternalContextAdapter context, int index) {
		if(node.jjtGetNumChildren() > index && node.jjtGetChild(index) != null) {
			return (T) node.jjtGetChild(index).value(context);
		}
		
		return null;
	}
	
	protected void writeLine(String line) throws IOException {
		this.writer.write(line);
		this.writer.write('\n');
	}
}
