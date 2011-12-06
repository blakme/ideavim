package com.maddyhome.idea.vim.helper;

/*
 * IdeaVim - A Vim emulator plugin for IntelliJ Idea
 * Copyright (C) 2003-2005 Rick Maddy
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.testFramework.LightVirtualFile;
import com.maddyhome.idea.vim.command.CommandState;
import com.maddyhome.idea.vim.command.VisualChange;
import com.maddyhome.idea.vim.command.VisualRange;

import java.util.Collection;

/**
 * This class is used to manipulate editor specific data. Each editor has a user defined map associated with it.
 * These methods provide convenient methods for working with that Vim Plugin specific data.
 */
public class EditorData {
  /**
   * This is used to initialize each new editor that gets created.
   *
   * @param editor The editor to initialize
   */
  public static void initializeEditor(Editor editor) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    if (logger.isDebugEnabled()) logger.debug("editor created: " + editor);
  }

  /**
   * This is used to clean up editors whenever they are closed.
   *
   * @param editor The editor to cleanup
   */
  public static void uninitializeEditor(Editor editor) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    if (logger.isDebugEnabled()) logger.debug("editor closed: " + editor);
    editor.putUserData(COMMAND_STATE, null);
    editor.putUserData(LAST_HIGHLIGHTS, null);
    editor.putUserData(VISUAL, null);
    editor.putUserData(VISUAL_OP, null);
  }

  /**
   * This gets the last column the cursor was in for the editor.
   *
   * @param editor The editr to get the last column from
   * @return Returns the last column as set by {@link #setLastColumn} or the current cursor column
   */
  public static int getLastColumn(Editor editor) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    Integer col = editor.getUserData(LAST_COLUMN);
    if (col == null) {
      return editor.getCaretModel().getVisualPosition().column;
    }
    else {
      return col;
    }
  }

  /**
   * Sets the last column for this editor
   *
   * @param col    The column
   * @param editor The editor
   */
  public static void setLastColumn(Editor editor, int col) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    editor.putUserData(LAST_COLUMN, col);
    int t = getLastColumn(editor);
    if (logger.isDebugEnabled()) logger.debug("setLastColumn(" + col + ") is now " + t);
  }

  public static String getLastSearch(Editor editor) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    return editor.getUserData(LAST_SEARCH);
  }

  public static void setLastSearch(Editor editor, String search) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    editor.putUserData(LAST_SEARCH, search);
  }

  public static Collection<RangeHighlighter> getLastHighlights(Editor editor) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    return editor.getUserData(LAST_HIGHLIGHTS);
  }

  public static void setLastHighlights(Editor editor, Collection<RangeHighlighter> highlights) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    editor.putUserData(LAST_HIGHLIGHTS, highlights);
  }

  /**
   * Gets the previous visual range for the editor.
   *
   * @param editor The editor to get the range for
   * @return The last visual range, null if no previous range
   */
  public static VisualRange getLastVisualRange(Editor editor) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    return editor.getDocument().getUserData(VISUAL);
  }

  /**
   * Sets the previous visual range for the editor.
   *
   * @param editor The editor to set the range for
   * @param range  The visual range
   */
  public static void setLastVisualRange(Editor editor, VisualRange range) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    editor.getDocument().putUserData(VISUAL, range);
  }

  /**
   * Gets the previous visual operator range for the editor.
   *
   * @param editor The editor to get the range for
   * @return The last visual range, null if no previous range
   */
  public static VisualChange getLastVisualOperatorRange(Editor editor) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    return editor.getDocument().getUserData(VISUAL_OP);
  }

  /**
   * Sets the previous visual operator range for the editor.
   *
   * @param editor The editor to set the range for
   * @param range  The visual range
   */
  public static void setLastVisualOperatorRange(Editor editor, VisualChange range) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    editor.getDocument().putUserData(VISUAL_OP, range);
  }

  public static CommandState getCommandState(Editor editor) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    return editor.getUserData(COMMAND_STATE);
  }

  public static void setCommandState(Editor editor, CommandState state) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    editor.putUserData(COMMAND_STATE, state);
  }

  public static boolean getChangeGroup(Editor editor) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    Boolean res = editor.getUserData(CHANGE_GROUP);
    if (res != null) {
      return res;
    }
    else {
      return false;
    }
  }

  public static void setChangeGroup(Editor editor, boolean adapter) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    editor.putUserData(CHANGE_GROUP, adapter);
  }

  public static boolean getMotionGroup(Editor editor) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    return editor.getUserData(MOTION_GROUP) == Boolean.TRUE;
  }

  public static void setMotionGroup(Editor editor, boolean adapter) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    editor.putUserData(MOTION_GROUP, adapter);
  }

  public static boolean isConsoleOutput(Editor editor) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    Object res = editor.getUserData(ConsoleViewImpl.CONSOLE_VIEW_IN_EDITOR_VIEW);
    logger.debug("isConsoleOutput for editor " + editor + " - " + res);
    return res != null;
  }

  /**
   * Gets the virtual file associated with this editor
   *
   * @param editor The editor
   * @return The virtual file for the editor
   */
  public static VirtualFile getVirtualFile(Editor editor) {
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    return FileDocumentManager.getInstance().getFile(editor.getDocument());
  }

  /**
   * This is a static helper - no instances needed
   */
  private EditorData() {
  }

  private static final Key<Integer> LAST_COLUMN = new Key<Integer>("lastColumn");
  private static final Key<VisualRange> VISUAL = new Key<VisualRange>("lastVisual");
  private static final Key<VisualChange> VISUAL_OP = new Key<VisualChange>("lastVisualOp");
  private static final Key<String> LAST_SEARCH = new Key<String>("lastSearch");
  private static final Key<Collection<RangeHighlighter>> LAST_HIGHLIGHTS = new Key<Collection<RangeHighlighter>>("lastHighlights");
  private static final Key<CommandState> COMMAND_STATE = new Key<CommandState>("commandState");
  private static final Key<Boolean> CHANGE_GROUP = new Key<Boolean>("changeGroup");
  private static final Key<Boolean> MOTION_GROUP = new Key<Boolean>("motionGroup");

  private static Logger logger = Logger.getInstance(EditorData.class.getName());

  /**
   * Checks if editor is file editor, also it takes into account that editor can be placed in editors hierarhy
   */
  public static boolean isFileEditor(Editor editor){
    editor = InjectedLanguageUtil.getTopLevelEditor(editor);
    final VirtualFile virtualFile = EditorData.getVirtualFile(editor);
    return virtualFile != null && !(virtualFile instanceof LightVirtualFile);
  }
}
