package org.amap.lafeedeschamps.domain.enumeration;

/**
 * The Type enumeration.
 */
public enum Type {
    DISTRIBUTION {
        @Override
        public String getDisplayText() {
            return "Distribution";
        }
    }, WORKSHOP {
        @Override
        public String getDisplayText() {
            return "Atelier";
        }
    };

    public abstract String getDisplayText();
}
