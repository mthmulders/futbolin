using System;
using System.Collections.Generic;

namespace Seedwork {
    public abstract class Entity {
        int _Id;

        public virtual int Id {
            get {
                return _Id;
            }
            protected set {
                _Id = value;
            }
        }

        public bool IsTransient() {
            return this.Id == default(Int32);
        }

        public override bool Equals(object obj) {
            if (obj == null || !(obj is Entity)) {
                return false;
            }
            if (Object.ReferenceEquals(this, obj)) {
                return true;
            }
            if (this.GetType() != obj.GetType()) {
                return false;
            }
            Entity item = (Entity) obj;
            if (item.IsTransient() || this.IsTransient()) {
                return false;
            } else {
                return item.Id == this.Id;
            }
        }

        public override int GetHashCode() {
            if (!IsTransient()) {
                // XOR for random distribution. See:
                //
                // http://blogs.msdn.com/b/ericlippert/archive/2011/02/28/guidelines-and-rules-for-gethashcode.aspx
                return this.Id.GetHashCode() ^ 31;
            } else {
                return base.GetHashCode();
            }
        }

        public static bool operator ==(Entity left, Entity right) {
            if (Object.Equals(left, null)) {
                // If both left and right are null, they are equal.
                return Object.Equals(right, null);
            } else {
                return left.Equals(right);
            }
        }

        public static bool operator !=(Entity left, Entity right) {
            return !(left == right);
        }
    }
}
