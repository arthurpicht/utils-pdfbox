package de.arthurpicht.utils.pdfbox;

import java.util.Collections;
import java.util.List;

public class TextBoxDimensions {

    private final float width;
    private final float height;
    private final List<Float> distanceBaselineToTop;

    public TextBoxDimensions(float width, float height, List<Float> distanceBaselineToTop) {
        this.width = width;
        this.height = height;
        this.distanceBaselineToTop = Collections.unmodifiableList(distanceBaselineToTop);
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public int getNrOfTextLines() {
        return this.distanceBaselineToTop.size();
    }

    public float getDistanceBaselineToTop(int baseLineIndex) {
        if (baseLineIndex >= this.distanceBaselineToTop.size() || baseLineIndex < 0)
            throw new IllegalArgumentException("baseLineIndex out of bounds.");
        return this.distanceBaselineToTop.get(baseLineIndex);
    }

}
