/*******************************************************************************
 * Copyright (c) 2014, Anton Gustafsson
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 
 * * Neither the name of Aquarria nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.github.antag99.aquarria.util;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Splits a tile image into the different frames
 */
public class FrameSplitter {
	private static ObjectMap<String, Rectangle> blockFrames = new ObjectMap<String, Rectangle>();
	private static ObjectMap<String, Rectangle> wallFrames = new ObjectMap<String, Rectangle>();

	static {
		blockFrames.put("empty", new Rectangle(162, 54, 16, 16));
		blockFrames.put("bottomRightCorner", new Rectangle(18, 72, 16, 16));
		blockFrames.put("bottomLeftCorner", new Rectangle(0, 72, 16, 16));
		blockFrames.put("topRightCorner", new Rectangle(18, 54, 16, 16));
		blockFrames.put("topLeftCorner", new Rectangle(0, 54, 16, 16));
		blockFrames.put("rightStrip", new Rectangle(162, 0, 16, 16));
		blockFrames.put("leftStrip", new Rectangle(216, 0, 16, 16));
		blockFrames.put("topStrip", new Rectangle(108, 54, 16, 16));
		blockFrames.put("bottomStrip", new Rectangle(108, 0, 16, 16));
		blockFrames.put("horizontalStrip", new Rectangle(108, 72, 16, 16));
		blockFrames.put("verticalStrip", new Rectangle(90, 0, 16, 16));
		blockFrames.put("full", new Rectangle(18, 18, 16, 16));
		blockFrames.put("bottomEdge", new Rectangle(18, 36, 16, 16));
		blockFrames.put("rightEdge", new Rectangle(72, 0, 16, 16));
		blockFrames.put("topEdge", new Rectangle(18, 0, 16, 16));
		blockFrames.put("leftEdge", new Rectangle(0, 0, 16, 16));

		wallFrames.put("empty", new Rectangle(324, 108, 32, 32));
		wallFrames.put("bottomRightCorner", new Rectangle(36, 144, 32, 32));
		wallFrames.put("bottomLeftCorner", new Rectangle(0, 144, 32, 32));
		wallFrames.put("topRightCorner", new Rectangle(36, 108, 32, 32));
		wallFrames.put("topLeftCorner", new Rectangle(0, 108, 32, 32));
		wallFrames.put("rightStrip", new Rectangle(324, 0, 32, 32));
		wallFrames.put("leftStrip", new Rectangle(432, 0, 32, 32));
		wallFrames.put("topStrip", new Rectangle(216, 108, 32, 32));
		wallFrames.put("bottomStrip", new Rectangle(216, 0, 32, 32));
		wallFrames.put("horizontalStrip", new Rectangle(216, 144, 32, 32));
		wallFrames.put("verticalStrip", new Rectangle(180, 0, 32, 32));
		wallFrames.put("full", new Rectangle(36, 36, 32, 32));
		wallFrames.put("bottomEdge", new Rectangle(36, 72, 32, 32));
		wallFrames.put("rightEdge", new Rectangle(144, 0, 32, 32));
		wallFrames.put("topEdge", new Rectangle(36, 0, 32, 32));
		wallFrames.put("leftEdge", new Rectangle(0, 0, 32, 32));
	}

	public enum SplitType {
		BLOCK,
		WALL;
	}

	private ObjectMap<String, Rectangle> frames;

	public FrameSplitter(SplitType splitType) {
		if (splitType == null) {
			throw new NullPointerException();
		}

		switch (splitType) {
		case BLOCK:
			frames = blockFrames;
			break;
		case WALL:
			frames = wallFrames;
			break;
		}
	}

	public void split(FileHandle sourceFile, FileHandle destinationDirectory) {
		Pixmap sourcePixmap = new Pixmap(sourceFile);
		for (String frameName : frames.keys()) {
			Rectangle frameBounds = frames.get(frameName);
			Pixmap destPixmap = new Pixmap((int) frameBounds.width, (int) frameBounds.height, Format.RGBA8888);
			destPixmap.drawPixmap(sourcePixmap, 0, 0,
					(int) frameBounds.x, (int) frameBounds.y,
					(int) frameBounds.width, (int) frameBounds.height);
			PixmapIO.writePNG(destinationDirectory.child(frameName + ".png"), destPixmap);
			destPixmap.dispose();
		}
		sourcePixmap.dispose();
	}
}