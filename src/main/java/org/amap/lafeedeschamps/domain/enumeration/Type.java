package org.amap.lafeedeschamps.domain.enumeration;

/**
 * The Type enumeration.
 */
public enum Type {
    DISTRIBUTION {
        @Override
        public String toString() {
            return "Distribution";
        }
    }, WORKSHOP {
        @Override
        public String toString() {
            return "Atelier";
        }
    }
}
