/***** Lobxxx Translate Finished ******/
package org.omg.DynamicAny;


/**
* org/omg/DynamicAny/DynAny.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from c:/re/workspace/8-2-build-windows-amd64-cygwin/jdk8u45/3627/corba/src/share/classes/org/omg/DynamicAny/DynamicAny.idl
* Thursday, April 30, 2015 12:42:08 PM PDT
*/


/**
    * Any values can be dynamically interpreted (traversed) and constructed through DynAny objects.
    * A DynAny object is associated with a data value which corresponds to a copy of the value
    * inserted into an any.
    * <P>A DynAny object may be viewed as an ordered collection of component DynAnys.
    * For DynAnys representing a basic type, such as long, or a type without components,
    * such as an empty exception, the ordered collection of components is empty.
    * Each DynAny object maintains the notion of a current position into its collection
    * of component DynAnys. The current position is identified by an index value that runs
    * from 0 to n-1, where n is the number of components.
    * The special index value -1 indicates a current position that points nowhere.
    * For values that cannot have a current position (such as an empty exception),
    * the index value is fixed at -1.
    * If a DynAny is initialized with a value that has components, the index is initialized to 0.
    * After creation of an uninitialized DynAny (that is, a DynAny that has no value but a TypeCode
    * that permits components), the current position depends on the type of value represented by
    * the DynAny. (The current position is set to 0 or -1, depending on whether the new DynAny
    * gets default values for its components.)
    * <P>The iteration operations rewind, seek, and next can be used to change the current position
    * and the current_component operation returns the component at the current position.
    * The component_count operation returns the number of components of a DynAny.
    * Collectively, these operations enable iteration over the components of a DynAny, for example,
    * to (recursively) examine its contents.
    * <P>A constructed DynAny object is a DynAny object associated with a constructed type.
    * There is a different interface, inheriting from the DynAny interface, associated with
    * each kind of constructed type in IDL (fixed, enum, struct, sequence, union, array,
    * exception, and value type).
    * <P>A constructed DynAny object exports operations that enable the creation of new DynAny objects,
    * each of them associated with a component of the constructed data value.
    * As an example, a DynStruct is associated with a struct value. This means that the DynStruct
    * may be seen as owning an ordered collection of components, one for each structure member.
    * The DynStruct object exports operations that enable the creation of new DynAny objects,
    * each of them associated with a member of the struct.
    * <P>If a DynAny object has been obtained from another (constructed) DynAny object,
    * such as a DynAny representing a structure member that was created from a DynStruct,
    * the member DynAny is logically contained in the DynStruct.
    * Calling an insert or get operation leaves the current position unchanged.
    * Destroying a top-level DynAny object (one that was not obtained as a component of another DynAny)
    * also destroys any component DynAny objects obtained from it.
    * Destroying a non-top level DynAny object does nothing.
    * Invoking operations on a destroyed top-level DynAny or any of its descendants raises OBJECT_NOT_EXIST.
    * If the programmer wants to destroy a DynAny object but still wants to manipulate some component
    * of the data value associated with it, then he or she should first create a DynAny for the component
    * and, after that, make a copy of the created DynAny object.
    * <P>The behavior of DynAny objects has been defined in order to enable efficient implementations
    * in terms of allocated memory space and speed of access. DynAny objects are intended to be used
    * for traversing values extracted from anys or constructing values of anys at runtime.
    * Their use for other purposes is not recommended.
    * <P>Insert and get operations are necessary to handle basic DynAny objects
    * but are also helpful to handle constructed DynAny objects.
    * Inserting a basic data type value into a constructed DynAny object
    * implies initializing the current component of the constructed data value
    * associated with the DynAny object. For example, invoking insert_boolean on a
    * DynStruct implies inserting a boolean data value at the current position
    * of the associated struct data value.
    * A type is consistent for inserting or extracting a value if its TypeCode is equivalent to
    * the TypeCode contained in the DynAny or, if the DynAny has components, is equivalent to the TypeCode
    * of the DynAny at the current position.
    * <P>DynAny and DynAnyFactory objects are intended to be local to the process in which they are
    * created and used. This means that references to DynAny and DynAnyFactory objects cannot be exported
    * to other processes, or externalized with ORB.object_to_string().
    * If any attempt is made to do so, the offending operation will raise a MARSHAL system exception.
    * Since their interfaces are specified in IDL, DynAny objects export operations defined in the standard
    * org.omg.CORBA.Object interface. However, any attempt to invoke operations exported through the Object
    * interface may raise the standard NO_IMPLEMENT exception.
    * An attempt to use a DynAny object with the DII may raise the NO_IMPLEMENT exception.
    * <p>
    * 任何值都可以动态解释(遍历)并通过DynAny对象构造DynAny对象与对应于插入到任何<P>中的值的副本的数据值相关联DynAny对象可以被视为组件的有序集合DynAnys对于表示基本类型(例如lon
    * g)或无组件类型(如空异常)的DynAnys,组件的有序集合为空每个DynAny对象将当前位置的概念保留到其组件DynAnys的集合中当前位置由从0到n-1的索引值来标识,其中n是分量的数目。
    * 特殊索引值-1指示指向无处的当前位置对于不能具有当前位置的值(例如空异常),索引值固定为-1如果使用具有组件的值初始化DynAny,则将索引初始化为0在创建未初始化的DynAny(即是一个没有值的Dyn
    * Any,但允许组件的TypeCode),当前位置取决于由DynAny表示的值的类型(当前位置设置为0或-1,这取决于新的DynAny是否获取默认值对于其组件)<P>迭代操作rewind,seek和nex
    * t可用于更改当前位置,current_component操作返回当前位置的组件。
    * component_count操作返回DynAny的组件数量总的来说,这些操作允许对DynAny的组件进行迭代,例如,(递归地)检查其内容。构造的DynAny对象是与构造类型相关联的DynAny对象。
    * 有一个不同的接口,继承自DynAny接口,与IDL中的每种构造类型相关联(固定,枚举,结构,序列,联合,数组,异常和值类型)构造的DynAny对象导出操作,可以创建新的DynAny对象,与构造的数据值的
    * 组件相关联作为示例,DynStruct与结构值相关联这意味着DynStruct可以被视为拥有组件的有序集合,每个结构成员DynStruct对象导出的操作允许创建新的DynAny对象,每个对象都与stru
    * ct <P>的成员相关联。
    * 如果DynAny对象是从另一个(构造的)DynAny对象获取的,例如表示结构成员的DynAny从DynStruct创建的成员DynAny在逻辑上包含在DynStruct中调用插入或获取操作保持当前位置不
    */
public interface DynAny extends DynAnyOperations, org.omg.CORBA.Object, org.omg.CORBA.portable.IDLEntity 
{
} // interface DynAny