/**
 * Generated by Gas3 v2.0.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR. INSTEAD, EDIT THE INHERITED CLASS (LocationType.as).
 */

package org.openwms.common.domain {

    import flash.events.EventDispatcher;
    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import flash.utils.IExternalizable;
    import flash.utils.getQualifiedClassName;
    import mx.collections.ListCollectionView;
    import mx.core.IUID;
    import mx.utils.UIDUtil;
    import org.granite.collections.IPersistentCollection;
    import org.granite.meta;
    import org.granite.tide.IEntity;
    import org.granite.tide.IEntityManager;
    import org.granite.tide.IPropertyHolder;

    use namespace meta;

    [Managed]
    public class LocationTypeBase implements IExternalizable, IUID {

        protected var _em:IEntityManager = null;

        private var __initialized:Boolean = true;
        private var __detachedState:String = null;

        private var _description:String;
        private var _height:int;
        private var _length:int;
        private var _locations:ListCollectionView;
        private var _type:String;
        private var _version:Number;
        private var _width:int;

        meta function isInitialized(name:String = null):Boolean {
            if (!name)
                return __initialized;

            var property:* = this[name];
            return (
                (!(property is LocationType) || (property as LocationType).meta::isInitialized()) &&
                (!(property is IPersistentCollection) || (property as IPersistentCollection).isInitialized())
            );
        }

        [Transient]
        public function meta_getEntityManager():IEntityManager {
            return _em;
        }
        public function meta_setEntityManager(em:IEntityManager):void {
            _em = em;
        }

        public function set description(value:String):void {
            _description = value;
        }
        public function get description():String {
            return _description;
        }

        public function set height(value:int):void {
            _height = value;
        }
        public function get height():int {
            return _height;
        }

        public function set length(value:int):void {
            _length = value;
        }
        public function get length():int {
            return _length;
        }

        public function set locations(value:ListCollectionView):void {
            _locations = value;
        }
        public function get locations():ListCollectionView {
            return _locations;
        }

        public function get type():String {
            return _type;
        }

        [Version]
        public function get version():Number {
            return _version;
        }

        public function set width(value:int):void {
            _width = value;
        }
        public function get width():int {
            return _width;
        }

        public function set uid(value:String):void {
            // noop...
        }
        public function get uid():String {
        	if (!_type)
        		return UIDUtil.createUID();
        	return getQualifiedClassName(this) + "#[" + String(_type) + "]";
        	
        }

        public function meta_merge(em:IEntityManager, obj:*):void {
            var src:LocationTypeBase = LocationTypeBase(obj);
            __initialized = src.__initialized;
            __detachedState = src.__detachedState;
            if (meta::isInitialized()) {
               em.meta_mergeExternal(src._description, _description, null, this, 'description', function setter(o:*):void{_description = o as String});
               em.meta_mergeExternal(src._height, _height, null, this, 'height', function setter(o:*):void{_height = o as int});
               em.meta_mergeExternal(src._length, _length, null, this, 'length', function setter(o:*):void{_length = o as int});
               em.meta_mergeExternal(src._locations, _locations, null, this, 'locations', function setter(o:*):void{_locations = o as ListCollectionView});
               em.meta_mergeExternal(src._type, _type, null, this, 'type', function setter(o:*):void{_type = o as String});
               em.meta_mergeExternal(src._version, _version, null, this, 'version', function setter(o:*):void{_version = o as Number});
               em.meta_mergeExternal(src._width, _width, null, this, 'width', function setter(o:*):void{_width = o as int});
            }
            else {
               em.meta_mergeExternal(src._type, _type, null, this, 'type', function setter(o:*):void{_type = o as String});
            }
        }

        public function readExternal(input:IDataInput):void {
            __initialized = input.readObject() as Boolean;
            __detachedState = input.readObject() as String;
            if (meta::isInitialized()) {
                _description = input.readObject() as String;
                _height = input.readObject() as int;
                _length = input.readObject() as int;
                _locations = input.readObject() as ListCollectionView;
                _type = input.readObject() as String;
                _version = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
                _width = input.readObject() as int;
            }
            else {
                _type = input.readObject() as String;
            }
        }

        public function writeExternal(output:IDataOutput):void {
            output.writeObject(__initialized);
            output.writeObject(__detachedState);
            if (meta::isInitialized()) {
                output.writeObject((_description is IPropertyHolder) ? IPropertyHolder(_description).object : _description);
                output.writeObject((_height is IPropertyHolder) ? IPropertyHolder(_height).object : _height);
                output.writeObject((_length is IPropertyHolder) ? IPropertyHolder(_length).object : _length);
                output.writeObject((_locations is IPropertyHolder) ? IPropertyHolder(_locations).object : _locations);
                output.writeObject((_type is IPropertyHolder) ? IPropertyHolder(_type).object : _type);
                output.writeObject((_version is IPropertyHolder) ? IPropertyHolder(_version).object : _version);
                output.writeObject((_width is IPropertyHolder) ? IPropertyHolder(_width).object : _width);
            }
            else {
                output.writeObject(_type);
            }
        }
    }
}