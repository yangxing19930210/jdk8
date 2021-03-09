/***** Lobxxx Translate Finished ******/
/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package java.awt;

import java.awt.peer.MenuItemPeer;
import java.awt.event.*;
import java.util.EventListener;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import javax.accessibility.*;
import sun.awt.AWTAccessor;

/**
 * All items in a menu must belong to the class
 * <code>MenuItem</code>, or one of its subclasses.
 * <p>
 * The default <code>MenuItem</code> object embodies
 * a simple labeled menu item.
 * <p>
 * This picture of a menu bar shows five menu items:
 * <IMG SRC="doc-files/MenuBar-1.gif" alt="The following text describes this graphic."
 * style="float:center; margin: 7px 10px;">
 * <br style="clear:left;">
 * The first two items are simple menu items, labeled
 * <code>"Basic"</code> and <code>"Simple"</code>.
 * Following these two items is a separator, which is itself
 * a menu item, created with the label <code>"-"</code>.
 * Next is an instance of <code>CheckboxMenuItem</code>
 * labeled <code>"Check"</code>. The final menu item is a
 * submenu labeled <code>"More&nbsp;Examples"</code>,
 * and this submenu is an instance of <code>Menu</code>.
 * <p>
 * When a menu item is selected, AWT sends an action event to
 * the menu item. Since the event is an
 * instance of <code>ActionEvent</code>, the <code>processEvent</code>
 * method examines the event and passes it along to
 * <code>processActionEvent</code>. The latter method redirects the
 * event to any <code>ActionListener</code> objects that have
 * registered an interest in action events generated by this
 * menu item.
 * <P>
 * Note that the subclass <code>Menu</code> overrides this behavior and
 * does not send any event to the frame until one of its subitems is
 * selected.
 *
 * <p>
 *  菜单中的所有项目必须属于类<code> MenuItem </code>或其子类之一。
 * <p>
 *  默认的<code> MenuItem </code>对象包含一个简单的标签菜单项。
 * <p>
 *  这个菜单栏的图片显示五个菜单项：<IMG SRC ="doc-files / MenuBar-1.gif"alt ="以下文本描述此图形。
 * style="float:center; margin: 7px 10px;">
 * <br style="clear:left;">
 *  前两个项目是简单的菜单项,标记为<code>"Basic"</code>和<code>"Simple"</code>。
 * 以下这两个项目是一个分隔符,它本身是一个菜单项,使用标签<code>" - "</code>创建。
 * 接下来是<code> CheckboxMenuItem </code>标记为<code>"Check"</code>的实例。
 * 最后一个菜单项是标记为<code>"More&nbsp; Examples"</code>的子菜单,此子菜单是<code> Menu </code>的一个实例。
 * <p>
 *  当选择菜单项时,AWT向该菜单项发送动作事件。
 * 由于事件是<code> ActionEvent </code>的一个实例,<code> processEvent </code>方法检查事件并将其传递给<code> processActionEvent
 *  </code>。
 *  当选择菜单项时,AWT向该菜单项发送动作事件。后一种方法将事件重定向到已注册了对此菜单项生成的动作事件感兴趣的任何<code> ActionListener </code>对象。
 * <P>
 *  请注意,子类<code> Menu </code>会覆盖此行为,并且不会向框架发送任何事件,直到选择其子项之一为止。
 * 
 * 
 * @author Sami Shaio
 */
public class MenuItem extends MenuComponent implements Accessible {

    static {
        /* ensure that the necessary native libraries are loaded */
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }

        AWTAccessor.setMenuItemAccessor(
            new AWTAccessor.MenuItemAccessor() {
                public boolean isEnabled(MenuItem item) {
                    return item.enabled;
                }

                public String getLabel(MenuItem item) {
                    return item.label;
                }

                public MenuShortcut getShortcut(MenuItem item) {
                    return item.shortcut;
                }

                public String getActionCommandImpl(MenuItem item) {
                    return item.getActionCommandImpl();
                }

                public boolean isItemEnabled(MenuItem item) {
                    return item.isItemEnabled();
                }
            });
    }

    /**
     * A value to indicate whether a menu item is enabled
     * or not.  If it is enabled, <code>enabled</code> will
     * be set to true.  Else <code>enabled</code> will
     * be set to false.
     *
     * <p>
     * 用于指示是否启用菜单项的值。如果启用,<code> enabled </code>将设置为true。 Else <code> enabled </code>将被设置为false。
     * 
     * 
     * @serial
     * @see #isEnabled()
     * @see #setEnabled(boolean)
     */
    boolean enabled = true;

    /**
     * <code>label</code> is the label of a menu item.
     * It can be any string.
     *
     * <p>
     *  <code> label </code>是菜单项的标签。它可以是任何字符串。
     * 
     * 
     * @serial
     * @see #getLabel()
     * @see #setLabel(String)
     */
    String label;

    /**
     * This field indicates the command tha has been issued
     * by a  particular menu item.
     * By default the <code>actionCommand</code>
     * is the label of the menu item, unless it has been
     * set using setActionCommand.
     *
     * <p>
     *  该字段表示由特定菜单项发出的命令。默认情况下,<code> actionCommand </code>是菜单项的标签,除非它已使用setActionCommand设置。
     * 
     * 
     * @serial
     * @see #setActionCommand(String)
     * @see #getActionCommand()
     */
    String actionCommand;

    /**
     * The eventMask is ONLY set by subclasses via enableEvents.
     * The mask should NOT be set when listeners are registered
     * so that we can distinguish the difference between when
     * listeners request events and subclasses request them.
     *
     * <p>
     *  eventMask仅由通过enableEvents的子类设置。当监听器被注册时,不应该设置掩码,这样我们可以区分监听器请求事件和子类请求它们之间的区别。
     * 
     * 
     * @serial
     */
    long eventMask;

    transient ActionListener actionListener;

    /**
     * A sequence of key stokes that ia associated with
     * a menu item.
     * Note :in 1.1.2 you must use setActionCommand()
     * on a menu item in order for its shortcut to
     * work.
     *
     * <p>
     *  与菜单项相关联的键序列的序列。注意：在1.1.2中,必须在菜单项上使用setActionCommand()才能使其快捷方式工作。
     * 
     * 
     * @serial
     * @see #getShortcut()
     * @see #setShortcut(MenuShortcut)
     * @see #deleteShortcut()
     */
    private MenuShortcut shortcut = null;

    private static final String base = "menuitem";
    private static int nameCounter = 0;

    /*
     * JDK 1.1 serialVersionUID
     * <p>
     *  JDK 1.1 serialVersionUID
     * 
     */
    private static final long serialVersionUID = -21757335363267194L;

    /**
     * Constructs a new MenuItem with an empty label and no keyboard
     * shortcut.
     * <p>
     *  构造一个带有空标签并且没有键盘快捷方式的新MenuItem。
     * 
     * 
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @since    JDK1.1
     */
    public MenuItem() throws HeadlessException {
        this("", null);
    }

    /**
     * Constructs a new MenuItem with the specified label
     * and no keyboard shortcut. Note that use of "-" in
     * a label is reserved to indicate a separator between
     * menu items. By default, all menu items except for
     * separators are enabled.
     * <p>
     *  构造具有指定标签并且没有键盘快捷方式的新MenuItem。注意,在标签中使用" - "保留以指示菜单项之间的分隔符。默认情况下,启用除分隔符之外的所有菜单项。
     * 
     * 
     * @param       label the label for this menu item.
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @since       JDK1.0
     */
    public MenuItem(String label) throws HeadlessException {
        this(label, null);
    }

    /**
     * Create a menu item with an associated keyboard shortcut.
     * Note that use of "-" in a label is reserved to indicate
     * a separator between menu items. By default, all menu
     * items except for separators are enabled.
     * <p>
     *  创建具有关联键盘快捷方式的菜单项。注意,在标签中使用" - "保留以指示菜单项之间的分隔符。默认情况下,启用除分隔符之外的所有菜单项。
     * 
     * 
     * @param       label the label for this menu item.
     * @param       s the instance of <code>MenuShortcut</code>
     *                       associated with this menu item.
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @since       JDK1.1
     */
    public MenuItem(String label, MenuShortcut s) throws HeadlessException {
        this.label = label;
        this.shortcut = s;
    }

    /**
     * Construct a name for this MenuComponent.  Called by getName() when
     * the name is null.
     * <p>
     *  为此MenuComponent构造名称。当名称为null时由getName()调用。
     * 
     */
    String constructComponentName() {
        synchronized (MenuItem.class) {
            return base + nameCounter++;
        }
    }

    /**
     * Creates the menu item's peer.  The peer allows us to modify the
     * appearance of the menu item without changing its functionality.
     * <p>
     * 创建菜单项的对等项。对等体允许我们修改菜单项的外观而不改变其功能。
     * 
     */
    public void addNotify() {
        synchronized (getTreeLock()) {
            if (peer == null)
                peer = Toolkit.getDefaultToolkit().createMenuItem(this);
        }
    }

    /**
     * Gets the label for this menu item.
     * <p>
     *  获取此菜单项的标签。
     * 
     * 
     * @return  the label of this menu item, or <code>null</code>
                       if this menu item has no label.
     * @see     java.awt.MenuItem#setLabel
     * @since   JDK1.0
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label for this menu item to the specified label.
     * <p>
     *  将此菜单项的标签设置为指定的标签。
     * 
     * 
     * @param     label   the new label, or <code>null</code> for no label.
     * @see       java.awt.MenuItem#getLabel
     * @since     JDK1.0
     */
    public synchronized void setLabel(String label) {
        this.label = label;
        MenuItemPeer peer = (MenuItemPeer)this.peer;
        if (peer != null) {
            peer.setLabel(label);
        }
    }

    /**
     * Checks whether this menu item is enabled.
     * <p>
     *  检查此菜单项是否已启用。
     * 
     * 
     * @see        java.awt.MenuItem#setEnabled
     * @since      JDK1.0
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether or not this menu item can be chosen.
     * <p>
     *  设置是否可以选择此菜单项。
     * 
     * 
     * @param      b  if <code>true</code>, enables this menu item;
     *                       if <code>false</code>, disables it.
     * @see        java.awt.MenuItem#isEnabled
     * @since      JDK1.1
     */
    public synchronized void setEnabled(boolean b) {
        enable(b);
    }

    /**
    /* <p>
    /* 
     * @deprecated As of JDK version 1.1,
     * replaced by <code>setEnabled(boolean)</code>.
     */
    @Deprecated
    public synchronized void enable() {
        enabled = true;
        MenuItemPeer peer = (MenuItemPeer)this.peer;
        if (peer != null) {
            peer.setEnabled(true);
        }
    }

    /**
    /* <p>
    /* 
     * @deprecated As of JDK version 1.1,
     * replaced by <code>setEnabled(boolean)</code>.
     */
    @Deprecated
    public void enable(boolean b) {
        if (b) {
            enable();
        } else {
            disable();
        }
    }

    /**
    /* <p>
    /* 
     * @deprecated As of JDK version 1.1,
     * replaced by <code>setEnabled(boolean)</code>.
     */
    @Deprecated
    public synchronized void disable() {
        enabled = false;
        MenuItemPeer peer = (MenuItemPeer)this.peer;
        if (peer != null) {
            peer.setEnabled(false);
        }
    }

    /**
     * Get the <code>MenuShortcut</code> object associated with this
     * menu item,
     * <p>
     *  获取与此菜单项相关联的<code> MenuShortcut </code>对象,
     * 
     * 
     * @return      the menu shortcut associated with this menu item,
     *                   or <code>null</code> if none has been specified.
     * @see         java.awt.MenuItem#setShortcut
     * @since       JDK1.1
     */
    public MenuShortcut getShortcut() {
        return shortcut;
    }

    /**
     * Set the <code>MenuShortcut</code> object associated with this
     * menu item. If a menu shortcut is already associated with
     * this menu item, it is replaced.
     * <p>
     *  设置与此菜单项关联的<code> MenuShortcut </code>对象。如果菜单快捷方式已与此菜单项相关联,则会被替换。
     * 
     * 
     * @param       s  the menu shortcut to associate
     *                           with this menu item.
     * @see         java.awt.MenuItem#getShortcut
     * @since       JDK1.1
     */
    public void setShortcut(MenuShortcut s) {
        shortcut = s;
        MenuItemPeer peer = (MenuItemPeer)this.peer;
        if (peer != null) {
            peer.setLabel(label);
        }
    }

    /**
     * Delete any <code>MenuShortcut</code> object associated
     * with this menu item.
     * <p>
     *  删除与此菜单项关联的任何<code> MenuShortcut </code>对象。
     * 
     * 
     * @since      JDK1.1
     */
    public void deleteShortcut() {
        shortcut = null;
        MenuItemPeer peer = (MenuItemPeer)this.peer;
        if (peer != null) {
            peer.setLabel(label);
        }
    }

    /*
     * Delete a matching MenuShortcut associated with this MenuItem.
     * Used when iterating Menus.
     * <p>
     *  删除与此MenuItem关联的匹配MenuShortcut。用于重复菜单。
     * 
     */
    void deleteShortcut(MenuShortcut s) {
        if (s.equals(shortcut)) {
            shortcut = null;
            MenuItemPeer peer = (MenuItemPeer)this.peer;
            if (peer != null) {
                peer.setLabel(label);
            }
        }
    }

    /*
     * The main goal of this method is to post an appropriate event
     * to the event queue when menu shortcut is pressed. However,
     * in subclasses this method may do more than just posting
     * an event.
     * <p>
     *  此方法的主要目的是在按下菜单快捷方式时将适当的事件发布到事件队列。然而,在子类中,该方法可以不仅仅发布事件。
     * 
     */
    void doMenuEvent(long when, int modifiers) {
        Toolkit.getEventQueue().postEvent(
            new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                            getActionCommand(), when, modifiers));
    }

    /*
     * Returns true if the item and all its ancestors are
     * enabled, false otherwise
     * <p>
     *  如果项目及其所有祖先启用,则返回true,否则返回false
     * 
     */
    private final boolean isItemEnabled() {
        // Fix For 6185151: Menu shortcuts of all menuitems within a menu
        // should be disabled when the menu itself is disabled
        if (!isEnabled()) {
            return false;
        }
        MenuContainer container = getParent_NoClientCode();
        do {
            if (!(container instanceof Menu)) {
                return true;
            }
            Menu menu = (Menu)container;
            if (!menu.isEnabled()) {
                return false;
            }
            container = menu.getParent_NoClientCode();
        } while (container != null);
        return true;
    }

    /*
     * Post an ActionEvent to the target (on
     * keydown) and the item is enabled.
     * Returns true if there is an associated shortcut.
     * <p>
     *  将ActionEvent发布到目标(在键下),并启用该项目。如果存在关联的快捷方式,则返回true。
     * 
     */
    boolean handleShortcut(KeyEvent e) {
        MenuShortcut s = new MenuShortcut(e.getKeyCode(),
                             (e.getModifiers() & InputEvent.SHIFT_MASK) > 0);
        MenuShortcut sE = new MenuShortcut(e.getExtendedKeyCode(),
                             (e.getModifiers() & InputEvent.SHIFT_MASK) > 0);
        // Fix For 6185151: Menu shortcuts of all menuitems within a menu
        // should be disabled when the menu itself is disabled
        if ((s.equals(shortcut) || sE.equals(shortcut)) && isItemEnabled()) {
            // MenuShortcut match -- issue an event on keydown.
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                doMenuEvent(e.getWhen(), e.getModifiers());
            } else {
                // silently eat key release.
            }
            return true;
        }
        return false;
    }

    MenuItem getShortcutMenuItem(MenuShortcut s) {
        return (s.equals(shortcut)) ? this : null;
    }

    /**
     * Enables event delivery to this menu item for events
     * to be defined by the specified event mask parameter
     * <p>
     * Since event types are automatically enabled when a listener for
     * that type is added to the menu item, this method only needs
     * to be invoked by subclasses of <code>MenuItem</code> which desire to
     * have the specified event types delivered to <code>processEvent</code>
     * regardless of whether a listener is registered.
     *
     * <p>
     *  使事件传递到此菜单项,以便由指定的事件掩码参数定义事件
     * <p>
     * 由于事件类型在将该类型的侦听器添加到菜单项时自动启用,所以此方法只需要由<code> MenuItem </code>的子类调用,它希望将指定的事件类型传递到<code> processEvent </code>
     * ,无论是否注册了监听器。
     * 
     * 
     * @param       eventsToEnable the event mask defining the event types
     * @see         java.awt.MenuItem#processEvent
     * @see         java.awt.MenuItem#disableEvents
     * @see         java.awt.Component#enableEvents
     * @since       JDK1.1
     */
    protected final void enableEvents(long eventsToEnable) {
        eventMask |= eventsToEnable;
        newEventsOnly = true;
    }

    /**
     * Disables event delivery to this menu item for events
     * defined by the specified event mask parameter.
     *
     * <p>
     *  针对由指定的事件掩码参数定义的事件,禁用事件传递到此菜单项。
     * 
     * 
     * @param       eventsToDisable the event mask defining the event types
     * @see         java.awt.MenuItem#processEvent
     * @see         java.awt.MenuItem#enableEvents
     * @see         java.awt.Component#disableEvents
     * @since       JDK1.1
     */
    protected final void disableEvents(long eventsToDisable) {
        eventMask &= ~eventsToDisable;
    }

    /**
     * Sets the command name of the action event that is fired
     * by this menu item.
     * <p>
     * By default, the action command is set to the label of
     * the menu item.
     * <p>
     *  设置此菜单项触发的操作事件的命令名。
     * <p>
     *  默认情况下,操作命令设置为菜单项的标签。
     * 
     * 
     * @param       command   the action command to be set
     *                                for this menu item.
     * @see         java.awt.MenuItem#getActionCommand
     * @since       JDK1.1
     */
    public void setActionCommand(String command) {
        actionCommand = command;
    }

    /**
     * Gets the command name of the action event that is fired
     * by this menu item.
     * <p>
     *  获取此菜单项触发的操作事件的命令名。
     * 
     * 
     * @see         java.awt.MenuItem#setActionCommand
     * @since       JDK1.1
     */
    public String getActionCommand() {
        return getActionCommandImpl();
    }

    // This is final so it can be called on the Toolkit thread.
    final String getActionCommandImpl() {
        return (actionCommand == null? label : actionCommand);
    }

    /**
     * Adds the specified action listener to receive action events
     * from this menu item.
     * If l is null, no exception is thrown and no action is performed.
     * <p>Refer to <a href="doc-files/AWTThreadIssues.html#ListenersThreads"
     * >AWT Threading Issues</a> for details on AWT's threading model.
     *
     * <p>
     *  添加指定的操作侦听器以从此菜单项接收操作事件。如果l为null,则不抛出异常,并且不执行任何操作。
     *  <p>有关AWT的线程模型的详细信息,请参阅<a href="doc-files/AWTThreadIssues.html#ListenersThreads"> AWT线程问题</a>。
     * 
     * 
     * @param      l the action listener.
     * @see        #removeActionListener
     * @see        #getActionListeners
     * @see        java.awt.event.ActionEvent
     * @see        java.awt.event.ActionListener
     * @since      JDK1.1
     */
    public synchronized void addActionListener(ActionListener l) {
        if (l == null) {
            return;
        }
        actionListener = AWTEventMulticaster.add(actionListener, l);
        newEventsOnly = true;
    }

    /**
     * Removes the specified action listener so it no longer receives
     * action events from this menu item.
     * If l is null, no exception is thrown and no action is performed.
     * <p>Refer to <a href="doc-files/AWTThreadIssues.html#ListenersThreads"
     * >AWT Threading Issues</a> for details on AWT's threading model.
     *
     * <p>
     *  删除指定的操作侦听器,使其不再从此菜单项接收操作事件。如果l为null,则不抛出异常,并且不执行任何操作。
     *  <p>有关AWT的线程模型的详细信息,请参阅<a href="doc-files/AWTThreadIssues.html#ListenersThreads"> AWT线程问题</a>。
     * 
     * 
     * @param      l the action listener.
     * @see        #addActionListener
     * @see        #getActionListeners
     * @see        java.awt.event.ActionEvent
     * @see        java.awt.event.ActionListener
     * @since      JDK1.1
     */
    public synchronized void removeActionListener(ActionListener l) {
        if (l == null) {
            return;
        }
        actionListener = AWTEventMulticaster.remove(actionListener, l);
    }

    /**
     * Returns an array of all the action listeners
     * registered on this menu item.
     *
     * <p>
     *  返回在此菜单项上注册的所有操作侦听器的数组。
     * 
     * 
     * @return all of this menu item's <code>ActionListener</code>s
     *         or an empty array if no action
     *         listeners are currently registered
     *
     * @see        #addActionListener
     * @see        #removeActionListener
     * @see        java.awt.event.ActionEvent
     * @see        java.awt.event.ActionListener
     * @since 1.4
     */
    public synchronized ActionListener[] getActionListeners() {
        return getListeners(ActionListener.class);
    }

    /**
     * Returns an array of all the objects currently registered
     * as <code><em>Foo</em>Listener</code>s
     * upon this <code>MenuItem</code>.
     * <code><em>Foo</em>Listener</code>s are registered using the
     * <code>add<em>Foo</em>Listener</code> method.
     *
     * <p>
     * You can specify the <code>listenerType</code> argument
     * with a class literal, such as
     * <code><em>Foo</em>Listener.class</code>.
     * For example, you can query a
     * <code>MenuItem</code> <code>m</code>
     * for its action listeners with the following code:
     *
     * <pre>ActionListener[] als = (ActionListener[])(m.getListeners(ActionListener.class));</pre>
     *
     * If no such listeners exist, this method returns an empty array.
     *
     * <p>
     * 返回当前在此<code> MenuItem </code>上注册为<code> <em> Foo </em> Listener </code>的所有对象的数组。
     * 使用<code> add <em> </em>侦听器</code>方法注册<code> <em> </em>侦听器</code>。
     * 
     * <p>
     *  您可以使用类文字指定<code> listenerType </code>参数,例如<code> <em> Foo </em> Listener.class </code>。
     * 例如,您可以使用以下代码查询<code> MenuItem </code> <code> m </code>作为其操作侦听器：。
     * 
     *  <pre> ActionListener [] als =(ActionListener [])(m.getListeners(ActionListener.class)); </pre>
     * 
     *  如果不存在此类侦听器,则此方法将返回一个空数组。
     * 
     * 
     * @param listenerType the type of listeners requested; this parameter
     *          should specify an interface that descends from
     *          <code>java.util.EventListener</code>
     * @return an array of all objects registered as
     *          <code><em>Foo</em>Listener</code>s on this menu item,
     *          or an empty array if no such
     *          listeners have been added
     * @exception ClassCastException if <code>listenerType</code>
     *          doesn't specify a class or interface that implements
     *          <code>java.util.EventListener</code>
     *
     * @see #getActionListeners
     * @since 1.3
     */
    public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
        EventListener l = null;
        if  (listenerType == ActionListener.class) {
            l = actionListener;
        }
        return AWTEventMulticaster.getListeners(l, listenerType);
    }

    /**
     * Processes events on this menu item. If the event is an
     * instance of <code>ActionEvent</code>, it invokes
     * <code>processActionEvent</code>, another method
     * defined by <code>MenuItem</code>.
     * <p>
     * Currently, menu items only support action events.
     * <p>Note that if the event parameter is <code>null</code>
     * the behavior is unspecified and may result in an
     * exception.
     *
     * <p>
     *  处理此菜单项上的事件。
     * 如果事件是<code> ActionEvent </code>的实例,它调用<code> processActionEvent </code>,由<code> MenuItem </code>定义的另一
     * 个方法。
     *  处理此菜单项上的事件。
     * <p>
     *  目前,菜单项只支持操作事件。 <p>请注意,如果事件参数为<code> null </code>,则此行为未指定,并可能导致异常。
     * 
     * 
     * @param       e the event
     * @see         java.awt.MenuItem#processActionEvent
     * @since       JDK1.1
     */
    protected void processEvent(AWTEvent e) {
        if (e instanceof ActionEvent) {
            processActionEvent((ActionEvent)e);
        }
    }

    // REMIND: remove when filtering is done at lower level
    boolean eventEnabled(AWTEvent e) {
        if (e.id == ActionEvent.ACTION_PERFORMED) {
            if ((eventMask & AWTEvent.ACTION_EVENT_MASK) != 0 ||
                actionListener != null) {
                return true;
            }
            return false;
        }
        return super.eventEnabled(e);
    }

    /**
     * Processes action events occurring on this menu item,
     * by dispatching them to any registered
     * <code>ActionListener</code> objects.
     * This method is not called unless action events are
     * enabled for this component. Action events are enabled
     * when one of the following occurs:
     * <ul>
     * <li>An <code>ActionListener</code> object is registered
     * via <code>addActionListener</code>.
     * <li>Action events are enabled via <code>enableEvents</code>.
     * </ul>
     * <p>Note that if the event parameter is <code>null</code>
     * the behavior is unspecified and may result in an
     * exception.
     *
     * <p>
     *  通过将它们分派到任何已注册的<code> ActionListener </code>对象来处理在此菜单项上发生的操作事件。除非为此组件启用了操作事件,否则不会调用此方法。
     * 当发生以下情况之一时,将启用操作事件：。
     * <ul>
     *  <li> <code> ActionListener </code>对象通过<code> addActionListener </code>注册。
     *  <li>操作事件通过<code> enableEvents </code>启用。
     * </ul>
     * <p>请注意,如果事件参数为<code> null </code>,则此行为未指定,并可能导致异常。
     * 
     * 
     * @param       e the action event
     * @see         java.awt.event.ActionEvent
     * @see         java.awt.event.ActionListener
     * @see         java.awt.MenuItem#enableEvents
     * @since       JDK1.1
     */
    protected void processActionEvent(ActionEvent e) {
        ActionListener listener = actionListener;
        if (listener != null) {
            listener.actionPerformed(e);
        }
    }

    /**
     * Returns a string representing the state of this <code>MenuItem</code>.
     * This method is intended to be used only for debugging purposes, and the
     * content and format of the returned string may vary between
     * implementations. The returned string may be empty but may not be
     * <code>null</code>.
     *
     * <p>
     *  返回表示此<code> MenuItem </code>的状态的字符串。此方法仅用于调试目的,并且返回的字符串的内容和格式可能因实现而异。
     * 返回的字符串可能为空,但可能不是<code> null </code>。
     * 
     * 
     * @return the parameter string of this menu item
     */
    public String paramString() {
        String str = ",label=" + label;
        if (shortcut != null) {
            str += ",shortcut=" + shortcut;
        }
        return super.paramString() + str;
    }


    /* Serialization support.
    /* <p>
     */

    /**
     * Menu item serialized data version.
     *
     * <p>
     *  菜单项序列化数据版本。
     * 
     * 
     * @serial
     */
    private int menuItemSerializedDataVersion = 1;

    /**
     * Writes default serializable fields to stream.  Writes
     * a list of serializable <code>ActionListeners</code>
     * as optional data. The non-serializable listeners are
     * detected and no attempt is made to serialize them.
     *
     * <p>
     *  将缺省可序列化字段写入流。将可序列化<code> ActionListeners </code>的列表写为可选数据。检测到不可序列化的侦听器,并且不尝试将它们串行化。
     * 
     * 
     * @param s the <code>ObjectOutputStream</code> to write
     * @serialData <code>null</code> terminated sequence of 0
     *   or more pairs; the pair consists of a <code>String</code>
     *   and an <code>Object</code>; the <code>String</code>
     *   indicates the type of object and is one of the following:
     *   <code>actionListenerK</code> indicating an
     *     <code>ActionListener</code> object
     *
     * @see AWTEventMulticaster#save(ObjectOutputStream, String, EventListener)
     * @see #readObject(ObjectInputStream)
     */
    private void writeObject(ObjectOutputStream s)
      throws IOException
    {
      s.defaultWriteObject();

      AWTEventMulticaster.save(s, actionListenerK, actionListener);
      s.writeObject(null);
    }

    /**
     * Reads the <code>ObjectInputStream</code> and if it
     * isn't <code>null</code> adds a listener to receive
     * action events fired by the <code>Menu</code> Item.
     * Unrecognized keys or values will be ignored.
     *
     * <p>
     *  读取<code> ObjectInputStream </code>,如果不是<code> null </code>添加了一个侦听器来接收由<code> Menu </code> Item触发的操作事
     * 件。
     * 无法识别的键或值将被忽略。
     * 
     * 
     * @param s the <code>ObjectInputStream</code> to read
     * @exception HeadlessException if
     *   <code>GraphicsEnvironment.isHeadless</code> returns
     *   <code>true</code>
     * @see #removeActionListener(ActionListener)
     * @see #addActionListener(ActionListener)
     * @see #writeObject(ObjectOutputStream)
     */
    private void readObject(ObjectInputStream s)
      throws ClassNotFoundException, IOException, HeadlessException
    {
      // HeadlessException will be thrown from MenuComponent's readObject
      s.defaultReadObject();

      Object keyOrNull;
      while(null != (keyOrNull = s.readObject())) {
        String key = ((String)keyOrNull).intern();

        if (actionListenerK == key)
          addActionListener((ActionListener)(s.readObject()));

        else // skip value for unrecognized key
          s.readObject();
      }
    }

    /**
     * Initialize JNI field and method IDs
     * <p>
     *  初始化JNI字段和方法ID
     * 
     */
    private static native void initIDs();


/////////////////
// Accessibility support
////////////////

    /**
     * Gets the AccessibleContext associated with this MenuItem.
     * For menu items, the AccessibleContext takes the form of an
     * AccessibleAWTMenuItem.
     * A new AccessibleAWTMenuItem instance is created if necessary.
     *
     * <p>
     *  获取与此MenuItem相关联的AccessibleContext。对于菜单项,AccessibleContext采用AccessibleAWTMenuItem的形式。
     * 如有必要,将创建一个新的AccessibleAWTMenuItem实例。
     * 
     * 
     * @return an AccessibleAWTMenuItem that serves as the
     *         AccessibleContext of this MenuItem
     * @since 1.3
     */
    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) {
            accessibleContext = new AccessibleAWTMenuItem();
        }
        return accessibleContext;
    }

    /**
     * Inner class of MenuItem used to provide default support for
     * accessibility.  This class is not meant to be used directly by
     * application developers, but is instead meant only to be
     * subclassed by menu component developers.
     * <p>
     * This class implements accessibility support for the
     * <code>MenuItem</code> class.  It provides an implementation of the
     * Java Accessibility API appropriate to menu item user-interface elements.
     * <p>
     *  MenuItem的内部类用于提供对辅助功能的默认支持。这个类不是直接由应用程序开发人员使用,而是意味着只能由菜单组件开发人员进行子类化。
     * <p>
     * 此类实现了<code> MenuItem </code>类的辅助功能支持。它提供了适用于菜单项用户界面元素的Java辅助功能API的实现。
     * 
     * 
     * @since 1.3
     */
    protected class AccessibleAWTMenuItem extends AccessibleAWTMenuComponent
        implements AccessibleAction, AccessibleValue
    {
        /*
         * JDK 1.3 serialVersionUID
         * <p>
         *  JDK 1.3 serialVersionUID
         * 
         */
        private static final long serialVersionUID = -217847831945965825L;

        /**
         * Get the accessible name of this object.
         *
         * <p>
         *  获取此对象的可访问名称。
         * 
         * 
         * @return the localized name of the object -- can be null if this
         * object does not have a name
         */
        public String getAccessibleName() {
            if (accessibleName != null) {
                return accessibleName;
            } else {
                if (getLabel() == null) {
                    return super.getAccessibleName();
                } else {
                    return getLabel();
                }
            }
        }

        /**
         * Get the role of this object.
         *
         * <p>
         *  获取此对象的作用。
         * 
         * 
         * @return an instance of AccessibleRole describing the role of the
         * object
         */
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.MENU_ITEM;
        }

        /**
         * Get the AccessibleAction associated with this object.  In the
         * implementation of the Java Accessibility API for this class,
         * return this object, which is responsible for implementing the
         * AccessibleAction interface on behalf of itself.
         *
         * <p>
         *  获取与此对象关联的AccessibleAction。在为该类实现Java辅助功能API时,返回此对象,该对象负责代表自身实现AccessibleAction接口。
         * 
         * 
         * @return this object
         */
        public AccessibleAction getAccessibleAction() {
            return this;
        }

        /**
         * Get the AccessibleValue associated with this object.  In the
         * implementation of the Java Accessibility API for this class,
         * return this object, which is responsible for implementing the
         * AccessibleValue interface on behalf of itself.
         *
         * <p>
         *  获取与此对象关联的AccessibleValue。在为该类实现Java Accessibility API时,返回此对象,该对象负责代表自身实现AccessibleValue接口。
         * 
         * 
         * @return this object
         */
        public AccessibleValue getAccessibleValue() {
            return this;
        }

        /**
         * Returns the number of Actions available in this object.  The
         * default behavior of a menu item is to have one action.
         *
         * <p>
         *  返回此对象中可用的操作数。菜单项的默认行为是具有一个动作。
         * 
         * 
         * @return 1, the number of Actions in this object
         */
        public int getAccessibleActionCount() {
            return 1;
        }

        /**
         * Return a description of the specified action of the object.
         *
         * <p>
         *  返回对象的指定操作的描述。
         * 
         * 
         * @param i zero-based index of the actions
         */
        public String getAccessibleActionDescription(int i) {
            if (i == 0) {
                // [[[PENDING:  WDW -- need to provide a localized string]]]
                return "click";
            } else {
                return null;
            }
        }

        /**
         * Perform the specified Action on the object
         *
         * <p>
         *  对对象执行指定的Action
         * 
         * 
         * @param i zero-based index of actions
         * @return true if the action was performed; otherwise false.
         */
        public boolean doAccessibleAction(int i) {
            if (i == 0) {
                // Simulate a button click
                Toolkit.getEventQueue().postEvent(
                        new ActionEvent(MenuItem.this,
                                        ActionEvent.ACTION_PERFORMED,
                                        MenuItem.this.getActionCommand(),
                                        EventQueue.getMostRecentEventTime(),
                                        0));
                return true;
            } else {
                return false;
            }
        }

        /**
         * Get the value of this object as a Number.
         *
         * <p>
         *  获取此对象的值作为数字。
         * 
         * 
         * @return An Integer of 0 if this isn't selected or an Integer of 1 if
         * this is selected.
         * @see javax.swing.AbstractButton#isSelected()
         */
        public Number getCurrentAccessibleValue() {
            return Integer.valueOf(0);
        }

        /**
         * Set the value of this object as a Number.
         *
         * <p>
         *  将此对象的值设置为Number。
         * 
         * 
         * @return True if the value was set.
         */
        public boolean setCurrentAccessibleValue(Number n) {
            return false;
        }

        /**
         * Get the minimum value of this object as a Number.
         *
         * <p>
         *  获取此对象的最小值作为数字。
         * 
         * 
         * @return An Integer of 0.
         */
        public Number getMinimumAccessibleValue() {
            return Integer.valueOf(0);
        }

        /**
         * Get the maximum value of this object as a Number.
         *
         * <p>
         *  获取此对象的最大值作为数字。
         * 
         * @return An Integer of 0.
         */
        public Number getMaximumAccessibleValue() {
            return Integer.valueOf(0);
        }

    } // class AccessibleAWTMenuItem

}
