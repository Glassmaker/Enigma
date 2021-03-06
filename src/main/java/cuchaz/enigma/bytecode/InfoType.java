/*******************************************************************************
 * Copyright (c) 2015 Jeff Martin.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public
 * License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Contributors:
 * Jeff Martin - initial API and implementation
 ******************************************************************************/
package cuchaz.enigma.bytecode;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;

import cuchaz.enigma.bytecode.accessors.*;

public enum InfoType {

    Utf8Info(1),
    IntegerInfo(3),
    FloatInfo(4),
    LongInfo(5),
    DoubleInfo(6),
    ClassInfo(7) {
        @Override
        public void gatherIndexTree(Collection<Integer> indices, ConstPoolEditor editor, ConstInfoAccessor entry) {
            ClassInfoAccessor accessor = new ClassInfoAccessor(entry.getItem());
            gatherIndexTree(indices, editor, accessor.getNameIndex());
        }

        @Override
        public void remapIndices(Map<Integer, Integer> map, ConstInfoAccessor entry) {
            ClassInfoAccessor accessor = new ClassInfoAccessor(entry.getItem());
            accessor.setNameIndex(remapIndex(map, accessor.getNameIndex()));
        }

        @Override
        public boolean subIndicesAreValid(ConstInfoAccessor entry, ConstPoolEditor pool) {
            ClassInfoAccessor accessor = new ClassInfoAccessor(entry.getItem());
            ConstInfoAccessor nameEntry = pool.getItem(accessor.getNameIndex());
            return nameEntry != null && nameEntry.getTag() == Utf8Info.getTag();
        }
    },
    StringInfo(8) {
        @Override
        public void gatherIndexTree(Collection<Integer> indices, ConstPoolEditor editor, ConstInfoAccessor entry) {
            StringInfoAccessor accessor = new StringInfoAccessor(entry.getItem());
            gatherIndexTree(indices, editor, accessor.getStringIndex());
        }

        @Override
        public void remapIndices(Map<Integer, Integer> map, ConstInfoAccessor entry) {
            StringInfoAccessor accessor = new StringInfoAccessor(entry.getItem());
            accessor.setStringIndex(remapIndex(map, accessor.getStringIndex()));
        }

        @Override
        public boolean subIndicesAreValid(ConstInfoAccessor entry, ConstPoolEditor pool) {
            StringInfoAccessor accessor = new StringInfoAccessor(entry.getItem());
            ConstInfoAccessor stringEntry = pool.getItem(accessor.getStringIndex());
            return stringEntry != null && stringEntry.getTag() == Utf8Info.getTag();
        }
    },
    FieldRefInfo(9) {
        @Override
        public void gatherIndexTree(Collection<Integer> indices, ConstPoolEditor editor, ConstInfoAccessor entry) {
            MemberRefInfoAccessor accessor = new MemberRefInfoAccessor(entry.getItem());
            gatherIndexTree(indices, editor, accessor.getClassIndex());
            gatherIndexTree(indices, editor, accessor.getNameAndTypeIndex());
        }

        @Override
        public void remapIndices(Map<Integer, Integer> map, ConstInfoAccessor entry) {
            MemberRefInfoAccessor accessor = new MemberRefInfoAccessor(entry.getItem());
            accessor.setClassIndex(remapIndex(map, accessor.getClassIndex()));
            accessor.setNameAndTypeIndex(remapIndex(map, accessor.getNameAndTypeIndex()));
        }

        @Override
        public boolean subIndicesAreValid(ConstInfoAccessor entry, ConstPoolEditor pool) {
            MemberRefInfoAccessor accessor = new MemberRefInfoAccessor(entry.getItem());
            ConstInfoAccessor classEntry = pool.getItem(accessor.getClassIndex());
            ConstInfoAccessor nameAndTypeEntry = pool.getItem(accessor.getNameAndTypeIndex());
            return classEntry != null && classEntry.getTag() == ClassInfo.getTag() && nameAndTypeEntry != null && nameAndTypeEntry.getTag() == NameAndTypeInfo.getTag();
        }
    },
    // same as FieldRefInfo
    MethodRefInfo(10) {
        @Override
        public void gatherIndexTree(Collection<Integer> indices, ConstPoolEditor editor, ConstInfoAccessor entry) {
            FieldRefInfo.gatherIndexTree(indices, editor, entry);
        }

        @Override
        public void remapIndices(Map<Integer, Integer> map, ConstInfoAccessor entry) {
            FieldRefInfo.remapIndices(map, entry);
        }

        @Override
        public boolean subIndicesAreValid(ConstInfoAccessor entry, ConstPoolEditor pool) {
            return FieldRefInfo.subIndicesAreValid(entry, pool);
        }
    },
    // same as FieldRefInfo
    InterfaceMethodRefInfo(11) {
        @Override
        public void gatherIndexTree(Collection<Integer> indices, ConstPoolEditor editor, ConstInfoAccessor entry) {
            FieldRefInfo.gatherIndexTree(indices, editor, entry);
        }

        @Override
        public void remapIndices(Map<Integer, Integer> map, ConstInfoAccessor entry) {
            FieldRefInfo.remapIndices(map, entry);
        }

        @Override
        public boolean subIndicesAreValid(ConstInfoAccessor entry, ConstPoolEditor pool) {
            return FieldRefInfo.subIndicesAreValid(entry, pool);
        }
    },
    NameAndTypeInfo(12) {
        @Override
        public void gatherIndexTree(Collection<Integer> indices, ConstPoolEditor editor, ConstInfoAccessor entry) {
            NameAndTypeInfoAccessor accessor = new NameAndTypeInfoAccessor(entry.getItem());
            gatherIndexTree(indices, editor, accessor.getNameIndex());
            gatherIndexTree(indices, editor, accessor.getTypeIndex());
        }

        @Override
        public void remapIndices(Map<Integer, Integer> map, ConstInfoAccessor entry) {
            NameAndTypeInfoAccessor accessor = new NameAndTypeInfoAccessor(entry.getItem());
            accessor.setNameIndex(remapIndex(map, accessor.getNameIndex()));
            accessor.setTypeIndex(remapIndex(map, accessor.getTypeIndex()));
        }

        @Override
        public boolean subIndicesAreValid(ConstInfoAccessor entry, ConstPoolEditor pool) {
            NameAndTypeInfoAccessor accessor = new NameAndTypeInfoAccessor(entry.getItem());
            ConstInfoAccessor nameEntry = pool.getItem(accessor.getNameIndex());
            ConstInfoAccessor typeEntry = pool.getItem(accessor.getTypeIndex());
            return nameEntry != null && nameEntry.getTag() == Utf8Info.getTag() && typeEntry != null && typeEntry.getTag() == Utf8Info.getTag();
        }
    },
    MethodHandleInfo(15) {
        @Override
        public void gatherIndexTree(Collection<Integer> indices, ConstPoolEditor editor, ConstInfoAccessor entry) {
            MethodHandleInfoAccessor accessor = new MethodHandleInfoAccessor(entry.getItem());
            gatherIndexTree(indices, editor, accessor.getTypeIndex());
            gatherIndexTree(indices, editor, accessor.getMethodRefIndex());
        }

        @Override
        public void remapIndices(Map<Integer, Integer> map, ConstInfoAccessor entry) {
            MethodHandleInfoAccessor accessor = new MethodHandleInfoAccessor(entry.getItem());
            accessor.setTypeIndex(remapIndex(map, accessor.getTypeIndex()));
            accessor.setMethodRefIndex(remapIndex(map, accessor.getMethodRefIndex()));
        }

        @Override
        public boolean subIndicesAreValid(ConstInfoAccessor entry, ConstPoolEditor pool) {
            MethodHandleInfoAccessor accessor = new MethodHandleInfoAccessor(entry.getItem());
            ConstInfoAccessor typeEntry = pool.getItem(accessor.getTypeIndex());
            ConstInfoAccessor methodRefEntry = pool.getItem(accessor.getMethodRefIndex());
            return typeEntry != null && typeEntry.getTag() == Utf8Info.getTag() && methodRefEntry != null && methodRefEntry.getTag() == MethodRefInfo.getTag();
        }
    },
    MethodTypeInfo(16) {
        @Override
        public void gatherIndexTree(Collection<Integer> indices, ConstPoolEditor editor, ConstInfoAccessor entry) {
            MethodTypeInfoAccessor accessor = new MethodTypeInfoAccessor(entry.getItem());
            gatherIndexTree(indices, editor, accessor.getTypeIndex());
        }

        @Override
        public void remapIndices(Map<Integer, Integer> map, ConstInfoAccessor entry) {
            MethodTypeInfoAccessor accessor = new MethodTypeInfoAccessor(entry.getItem());
            accessor.setTypeIndex(remapIndex(map, accessor.getTypeIndex()));
        }

        @Override
        public boolean subIndicesAreValid(ConstInfoAccessor entry, ConstPoolEditor pool) {
            MethodTypeInfoAccessor accessor = new MethodTypeInfoAccessor(entry.getItem());
            ConstInfoAccessor typeEntry = pool.getItem(accessor.getTypeIndex());
            return typeEntry != null && typeEntry.getTag() == Utf8Info.getTag();
        }
    },
    InvokeDynamicInfo(18) {
        @Override
        public void gatherIndexTree(Collection<Integer> indices, ConstPoolEditor editor, ConstInfoAccessor entry) {
            InvokeDynamicInfoAccessor accessor = new InvokeDynamicInfoAccessor(entry.getItem());
            gatherIndexTree(indices, editor, accessor.getBootstrapIndex());
            gatherIndexTree(indices, editor, accessor.getNameAndTypeIndex());
        }

        @Override
        public void remapIndices(Map<Integer, Integer> map, ConstInfoAccessor entry) {
            InvokeDynamicInfoAccessor accessor = new InvokeDynamicInfoAccessor(entry.getItem());
            accessor.setBootstrapIndex(remapIndex(map, accessor.getBootstrapIndex()));
            accessor.setNameAndTypeIndex(remapIndex(map, accessor.getNameAndTypeIndex()));
        }

        @Override
        public boolean subIndicesAreValid(ConstInfoAccessor entry, ConstPoolEditor pool) {
            InvokeDynamicInfoAccessor accessor = new InvokeDynamicInfoAccessor(entry.getItem());
            ConstInfoAccessor bootstrapEntry = pool.getItem(accessor.getBootstrapIndex());
            ConstInfoAccessor nameAndTypeEntry = pool.getItem(accessor.getNameAndTypeIndex());
            return bootstrapEntry != null && bootstrapEntry.getTag() == Utf8Info.getTag() && nameAndTypeEntry != null && nameAndTypeEntry.getTag() == NameAndTypeInfo.getTag();
        }
    };

    private static Map<Integer, InfoType> types;

    static {
        types = Maps.newTreeMap();
        for (InfoType type : values()) {
            types.put(type.getTag(), type);
        }
    }

    private int tag;

    InfoType(int tag) {
        this.tag = tag;
    }

    public int getTag() {
        return this.tag;
    }

    public void gatherIndexTree(Collection<Integer> indices, ConstPoolEditor editor, ConstInfoAccessor entry) {
        // by default, do nothing
    }

    public void remapIndices(Map<Integer, Integer> map, ConstInfoAccessor entry) {
        // by default, do nothing
    }

    public boolean subIndicesAreValid(ConstInfoAccessor entry, ConstPoolEditor pool) {
        // by default, everything is good
        return true;
    }

    public static InfoType getByTag(int tag) {
        return types.get(tag);
    }

    public static void gatherIndexTree(Collection<Integer> indices, ConstPoolEditor editor, int index) {
        // add own index
        indices.add(index);

        // recurse
        ConstInfoAccessor entry = editor.getItem(index);
        entry.getType().gatherIndexTree(indices, editor, entry);
    }

    private static int remapIndex(Map<Integer, Integer> map, int index) {
        Integer newIndex = map.get(index);
        if (newIndex == null) {
            newIndex = index;
        }
        return newIndex;
    }
}
