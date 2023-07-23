// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.texture;

public enum TextureType
{
    TEXTURE_1D(3552), 
    TEXTURE_2D(3553), 
    TEXTURE_3D(32879), 
    TEXTURE_RECTANGLE(34037);
    
    private int id;
    
    private TextureType(final int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
    
    private static /* synthetic */ TextureType[] $values() {
        return new TextureType[] { TextureType.TEXTURE_1D, TextureType.TEXTURE_2D, TextureType.TEXTURE_3D, TextureType.TEXTURE_RECTANGLE };
    }
    
    static {
        $VALUES = $values();
    }
}
