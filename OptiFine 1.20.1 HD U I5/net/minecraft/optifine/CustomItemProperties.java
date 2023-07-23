// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import java.util.Optional;
import com.mojang.datafixers.util.Either;
import java.util.LinkedHashSet;
import org.lwjgl.opengl.GL11;
import com.mojang.blaze3d.platform.GlStateManager;
import java.util.function.Function;
import net.optifine.util.StrUtils;
import java.util.HashMap;
import net.optifine.config.RangeInt;
import java.util.List;
import java.util.ArrayList;
import net.optifine.util.TextureUtils;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TreeSet;
import net.optifine.render.Blender;
import net.optifine.config.IParserInt;
import net.optifine.config.ParserEnchantmentId;
import java.util.Properties;
import java.util.Set;
import net.optifine.config.NbtTagValue;
import net.optifine.config.RangeListInt;
import java.util.Map;

public class CustomItemProperties
{
    public String name;
    public String basePath;
    public int type;
    public int[] items;
    public String texture;
    public Map<String, String> mapTextures;
    public String model;
    public Map<String, String> mapModels;
    public RangeListInt damage;
    public boolean damagePercent;
    public int damageMask;
    public RangeListInt stackSize;
    public int[] enchantmentIds;
    public RangeListInt enchantmentLevels;
    public NbtTagValue[] nbtTagValues;
    public int hand;
    public int blend;
    public float speed;
    public float rotation;
    public int layer;
    public float duration;
    public int weight;
    public acq textureLocation;
    public Map mapTextureLocations;
    public fuv sprite;
    public Map mapSprites;
    public fwr bakedModelTexture;
    public Map<String, fwr> mapBakedModelsTexture;
    public fwr bakedModelFull;
    public Map<String, fwr> mapBakedModelsFull;
    public Set<acq> modelSpriteLocations;
    private int textureWidth;
    private int textureHeight;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_ENCHANTMENT = 2;
    public static final int TYPE_ARMOR = 3;
    public static final int TYPE_ELYTRA = 4;
    public static final int HAND_ANY = 0;
    public static final int HAND_MAIN = 1;
    public static final int HAND_OFF = 2;
    public static final String INVENTORY = "inventory";
    
    public CustomItemProperties(final Properties props, final String path) {
        this.name = null;
        this.basePath = null;
        this.type = 1;
        this.items = null;
        this.texture = null;
        this.mapTextures = null;
        this.model = null;
        this.mapModels = null;
        this.damage = null;
        this.damagePercent = false;
        this.damageMask = 0;
        this.stackSize = null;
        this.enchantmentIds = null;
        this.enchantmentLevels = null;
        this.nbtTagValues = null;
        this.hand = 0;
        this.blend = 1;
        this.speed = 0.0f;
        this.rotation = 0.0f;
        this.layer = 0;
        this.duration = 1.0f;
        this.weight = 0;
        this.textureLocation = null;
        this.mapTextureLocations = null;
        this.sprite = null;
        this.mapSprites = null;
        this.bakedModelTexture = null;
        this.mapBakedModelsTexture = null;
        this.bakedModelFull = null;
        this.mapBakedModelsFull = null;
        this.modelSpriteLocations = null;
        this.textureWidth = 0;
        this.textureHeight = 0;
        this.name = parseName(path);
        this.basePath = parseBasePath(path);
        this.type = this.parseType(props.getProperty("type"));
        this.items = this.parseItems(props.getProperty("items"), props.getProperty("matchItems"));
        this.mapModels = (Map<String, String>)parseModels(props, this.basePath);
        this.model = parseModel(props.getProperty("model"), path, this.basePath, this.type, this.mapModels);
        this.mapTextures = (Map<String, String>)parseTextures(props, this.basePath);
        final boolean textureFromPath = this.mapModels == null && this.model == null;
        this.texture = parseTexture(props.getProperty("texture"), props.getProperty("tile"), props.getProperty("source"), path, this.basePath, this.type, this.mapTextures, textureFromPath);
        String damageStr = props.getProperty("damage");
        if (damageStr != null) {
            this.damagePercent = damageStr.contains("%");
            damageStr = damageStr.replace("%", "");
            this.damage = this.parseRangeListInt(damageStr);
            this.damageMask = this.parseInt(props.getProperty("damageMask"), 0);
        }
        this.stackSize = this.parseRangeListInt(props.getProperty("stackSize"));
        this.enchantmentIds = this.parseInts(getProperty(props, "enchantmentIDs", "enchantments"), new ParserEnchantmentId());
        this.enchantmentLevels = this.parseRangeListInt(props.getProperty("enchantmentLevels"));
        this.nbtTagValues = this.parseNbtTagValues(props);
        this.hand = this.parseHand(props.getProperty("hand"));
        this.blend = Blender.parseBlend(props.getProperty("blend"));
        this.speed = this.parseFloat(props.getProperty("speed"), 0.0f);
        this.rotation = this.parseFloat(props.getProperty("rotation"), 0.0f);
        this.layer = this.parseInt(props.getProperty("layer"), 0);
        this.weight = this.parseInt(props.getProperty("weight"), 0);
        this.duration = this.parseFloat(props.getProperty("duration"), 1.0f);
    }
    
    private static String getProperty(final Properties props, final String... names) {
        for (int i = 0; i < names.length; ++i) {
            final String name = names[i];
            final String val = props.getProperty(name);
            if (val != null) {
                return val;
            }
        }
        return null;
    }
    
    private static String parseName(final String path) {
        String str = path;
        final int pos = str.lastIndexOf(47);
        if (pos >= 0) {
            str = str.substring(pos + 1);
        }
        final int pos2 = str.lastIndexOf(46);
        if (pos2 >= 0) {
            str = str.substring(0, pos2);
        }
        return str;
    }
    
    private static String parseBasePath(final String path) {
        final int pos = path.lastIndexOf(47);
        if (pos < 0) {
            return "";
        }
        return path.substring(0, pos);
    }
    
    private int parseType(final String str) {
        if (str == null) {
            return 1;
        }
        if (str.equals("item")) {
            return 1;
        }
        if (str.equals("enchantment")) {
            return 2;
        }
        if (str.equals("armor")) {
            return 3;
        }
        if (str.equals("elytra")) {
            return 4;
        }
        Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, str));
        return 0;
    }
    
    private int[] parseItems(String str, final String str2) {
        if (str == null) {
            str = str2;
        }
        if (str == null) {
            return null;
        }
        str = str.trim();
        final Set setItemIds = new TreeSet();
        final String[] tokens = Config.tokenize(str, " ");
        for (int i = 0; i < tokens.length; ++i) {
            final String token = tokens[i];
            final cfu item = this.getItemByName(token);
            if (item == null) {
                Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, token));
            }
            else {
                final int id = cfu.a(item);
                if (id < 0) {
                    Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, token));
                }
                else {
                    setItemIds.add(new Integer(id));
                }
            }
        }
        final Integer[] integers = setItemIds.toArray(new Integer[setItemIds.size()]);
        final int[] ints = new int[integers.length];
        for (int j = 0; j < ints.length; ++j) {
            ints[j] = integers[j];
        }
        return ints;
    }
    
    private cfu getItemByName(final String name) {
        final acq loc = new acq(name);
        if (!jb.i.c(loc)) {
            return null;
        }
        final cfu item = (cfu)jb.i.a(loc);
        return item;
    }
    
    private static String parseTexture(String texStr, final String texStr2, final String texStr3, final String path, final String basePath, final int type, final Map<String, String> mapTexs, final boolean textureFromPath) {
        if (texStr == null) {
            texStr = texStr2;
        }
        if (texStr == null) {
            texStr = texStr3;
        }
        if (texStr != null) {
            final String png = ".png";
            if (texStr.endsWith(png)) {
                texStr = texStr.substring(0, texStr.length() - png.length());
            }
            texStr = fixTextureName(texStr, basePath);
            return texStr;
        }
        if (type == 3) {
            return null;
        }
        if (mapTexs != null) {
            final String bowStandbyTex = mapTexs.get("texture.bow_standby");
            if (bowStandbyTex != null) {
                return bowStandbyTex;
            }
        }
        if (!textureFromPath) {
            return null;
        }
        String str = path;
        final int pos = str.lastIndexOf(47);
        if (pos >= 0) {
            str = str.substring(pos + 1);
        }
        final int pos2 = str.lastIndexOf(46);
        if (pos2 >= 0) {
            str = str.substring(0, pos2);
        }
        str = fixTextureName(str, basePath);
        return str;
    }
    
    private static Map parseTextures(final Properties props, final String basePath) {
        final String prefix = "texture.";
        final Map mapProps = getMatchingProperties(props, prefix);
        if (mapProps.size() <= 0) {
            return null;
        }
        final Set keySet = mapProps.keySet();
        final Map mapTex = new LinkedHashMap();
        for (final String key : keySet) {
            String val = mapProps.get(key);
            val = fixTextureName(val, basePath);
            mapTex.put(key, val);
        }
        return mapTex;
    }
    
    private static String fixTextureName(String iconName, final String basePath) {
        iconName = TextureUtils.fixResourcePath(iconName, basePath);
        if (!iconName.startsWith(basePath) && !iconName.startsWith("textures/") && !iconName.startsWith("optifine/")) {
            iconName = invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, basePath, iconName);
        }
        if (iconName.endsWith(".png")) {
            iconName = iconName.substring(0, iconName.length() - 4);
        }
        if (iconName.startsWith("/")) {
            iconName = iconName.substring(1);
        }
        return iconName;
    }
    
    private static String parseModel(String modelStr, final String path, final String basePath, final int type, final Map<String, String> mapModelNames) {
        if (modelStr != null) {
            final String json = ".json";
            if (modelStr.endsWith(json)) {
                modelStr = modelStr.substring(0, modelStr.length() - json.length());
            }
            modelStr = fixModelName(modelStr, basePath);
            return modelStr;
        }
        if (type == 3) {
            return null;
        }
        if (mapModelNames != null) {
            final String bowStandbyModel = mapModelNames.get("model.bow_standby");
            if (bowStandbyModel != null) {
                return bowStandbyModel;
            }
        }
        return modelStr;
    }
    
    private static Map parseModels(final Properties props, final String basePath) {
        final String prefix = "model.";
        final Map mapProps = getMatchingProperties(props, prefix);
        if (mapProps.size() <= 0) {
            return null;
        }
        final Set keySet = mapProps.keySet();
        final Map mapTex = new LinkedHashMap();
        for (final String key : keySet) {
            String val = mapProps.get(key);
            val = fixModelName(val, basePath);
            mapTex.put(key, val);
        }
        return mapTex;
    }
    
    private static String fixModelName(String modelName, final String basePath) {
        modelName = TextureUtils.fixResourcePath(modelName, basePath);
        final boolean isVanilla = modelName.startsWith("block/") || modelName.startsWith("item/");
        if (!modelName.startsWith(basePath) && !isVanilla && !modelName.startsWith("optifine/")) {
            modelName = invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, basePath, modelName);
        }
        final String json = ".json";
        if (modelName.endsWith(json)) {
            modelName = modelName.substring(0, modelName.length() - json.length());
        }
        if (modelName.startsWith("/")) {
            modelName = modelName.substring(1);
        }
        return modelName;
    }
    
    private int parseInt(String str, final int defVal) {
        if (str == null) {
            return defVal;
        }
        str = str.trim();
        final int val = Config.parseInt(str, Integer.MIN_VALUE);
        if (val == Integer.MIN_VALUE) {
            Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, str));
            return defVal;
        }
        return val;
    }
    
    private float parseFloat(String str, final float defVal) {
        if (str == null) {
            return defVal;
        }
        str = str.trim();
        final float val = Config.parseFloat(str, Float.MIN_VALUE);
        if (val == Float.MIN_VALUE) {
            Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, str));
            return defVal;
        }
        return val;
    }
    
    private int[] parseInts(final String str, final IParserInt parser) {
        if (str == null) {
            return null;
        }
        final String[] tokens = Config.tokenize(str, " ");
        final List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < tokens.length; ++i) {
            final String token = tokens[i];
            final int val = parser.parse(token, Integer.MIN_VALUE);
            if (val == Integer.MIN_VALUE) {
                Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, token));
            }
            else {
                list.add(val);
            }
        }
        final Integer[] intArr = list.toArray(new Integer[list.size()]);
        final int[] ints = Config.toPrimitive(intArr);
        return ints;
    }
    
    private RangeListInt parseRangeListInt(final String str) {
        if (str == null) {
            return null;
        }
        final String[] tokens = Config.tokenize(str, " ");
        final RangeListInt rangeList = new RangeListInt();
        for (int i = 0; i < tokens.length; ++i) {
            final String token = tokens[i];
            final RangeInt range = this.parseRangeInt(token);
            if (range == null) {
                Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, str));
                return null;
            }
            rangeList.addRange(range);
        }
        return rangeList;
    }
    
    private RangeInt parseRangeInt(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        final int countMinus = str.length() - str.replace("-", "").length();
        if (countMinus > 1) {
            Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, str));
            return null;
        }
        final String[] tokens = Config.tokenize(str, "- ");
        final int[] vals = new int[tokens.length];
        for (int i = 0; i < tokens.length; ++i) {
            final String token = tokens[i];
            final int val = Config.parseInt(token, -1);
            if (val < 0) {
                Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, str));
                return null;
            }
            vals[i] = val;
        }
        if (vals.length == 1) {
            final int val2 = vals[0];
            if (str.startsWith("-")) {
                return new RangeInt(0, val2);
            }
            if (str.endsWith("-")) {
                return new RangeInt(val2, 65535);
            }
            return new RangeInt(val2, val2);
        }
        else {
            if (vals.length == 2) {
                final int min = Math.min(vals[0], vals[1]);
                final int max = Math.max(vals[0], vals[1]);
                return new RangeInt(min, max);
            }
            Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, str));
            return null;
        }
    }
    
    private NbtTagValue[] parseNbtTagValues(final Properties props) {
        final String PREFIX_NBT = "nbt.";
        final Map mapNbt = getMatchingProperties(props, PREFIX_NBT);
        if (mapNbt.size() <= 0) {
            return null;
        }
        final List listNbts = new ArrayList();
        final Set keySet = mapNbt.keySet();
        for (final String key : keySet) {
            final String val = mapNbt.get(key);
            final String id = key.substring(PREFIX_NBT.length());
            final NbtTagValue nbt = new NbtTagValue(id, val);
            listNbts.add(nbt);
        }
        final NbtTagValue[] nbts = listNbts.toArray(new NbtTagValue[listNbts.size()]);
        return nbts;
    }
    
    private static Map getMatchingProperties(final Properties props, final String keyPrefix) {
        final Map map = new LinkedHashMap();
        final Set keySet = props.keySet();
        for (final String key : keySet) {
            final String val = props.getProperty(key);
            if (key.startsWith(keyPrefix)) {
                map.put(key, val);
            }
        }
        return map;
    }
    
    private int parseHand(String str) {
        if (str == null) {
            return 0;
        }
        str = str.toLowerCase();
        if (str.equals("any")) {
            return 0;
        }
        if (str.equals("main")) {
            return 1;
        }
        if (str.equals("off")) {
            return 2;
        }
        Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, str));
        return 0;
    }
    
    public boolean isValid(final String path) {
        if (this.name == null || this.name.length() <= 0) {
            Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, path));
            return false;
        }
        if (this.basePath == null) {
            Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, path));
            return false;
        }
        if (this.type == 0) {
            Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, path));
            return false;
        }
        if (this.type == 4 && this.items == null) {
            this.items = new int[] { cfu.a(cgc.nh) };
        }
        if (this.type == 1 || this.type == 3 || this.type == 4) {
            if (this.items == null) {
                this.items = this.detectItems();
            }
            if (this.items == null) {
                Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, path));
                return false;
            }
        }
        if (this.texture == null && this.mapTextures == null && this.model == null && this.mapModels == null) {
            Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, path));
            return false;
        }
        if (this.type == 2 && this.enchantmentIds == null) {
            Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, path));
            return false;
        }
        return true;
    }
    
    private int[] detectItems() {
        final cfu item = this.getItemByName(this.name);
        if (item == null) {
            return null;
        }
        final int id = cfu.a(item);
        if (id < 0) {
            return null;
        }
        return new int[] { id };
    }
    
    public void updateIcons(final fuu textureMap) {
        if (this.texture != null) {
            this.textureLocation = this.getTextureLocation(this.texture);
            if (this.type == 1) {
                final acq spriteLocation = this.getSpriteLocation(this.textureLocation);
                this.sprite = textureMap.registerSprite(spriteLocation);
            }
        }
        if (this.mapTextures != null) {
            this.mapTextureLocations = new HashMap();
            this.mapSprites = new HashMap();
            final Set keySet = this.mapTextures.keySet();
            for (final String key : keySet) {
                final String val = this.mapTextures.get(key);
                final acq locTex = this.getTextureLocation(val);
                this.mapTextureLocations.put(key, locTex);
                if (this.type == 1) {
                    final acq locSprite = this.getSpriteLocation(locTex);
                    final fuv icon = textureMap.registerSprite(locSprite);
                    this.mapSprites.put(key, icon);
                }
            }
        }
        for (final acq loc : this.modelSpriteLocations) {
            textureMap.registerSprite(loc);
        }
    }
    
    public void refreshIcons(final fuu textureMap) {
        if (this.sprite != null) {
            this.sprite = textureMap.a(this.sprite.getName());
        }
        if (this.mapSprites != null) {
            final Set keySet = this.mapSprites.keySet();
            for (final String key : keySet) {
                final fuv sprite = this.mapSprites.get(key);
                if (sprite == null) {
                    continue;
                }
                final acq loc = sprite.getName();
                final fuv spriteNew = textureMap.a(loc);
                if (spriteNew == null || ful.isMisingSprite(spriteNew)) {
                    Config.warn(invokedynamic(makeConcatWithConstants:(Lacq;Ljava/lang/String;)Ljava/lang/String;, loc, this.basePath));
                }
                this.mapSprites.put(key, spriteNew);
            }
        }
    }
    
    private acq getTextureLocation(final String texName) {
        if (texName == null) {
            return null;
        }
        final acq resLoc = new acq(texName);
        final String domain = resLoc.b();
        String path = resLoc.a();
        if (!path.contains("/")) {
            path = invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, path);
        }
        final String filePath = invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, path);
        final acq locFile = new acq(domain, filePath);
        final boolean exists = Config.hasResource(locFile);
        if (!exists) {
            Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, filePath));
        }
        return locFile;
    }
    
    private acq getSpriteLocation(final acq resLoc) {
        String pathTex = resLoc.a();
        pathTex = StrUtils.removePrefix(pathTex, "textures/");
        pathTex = StrUtils.removeSuffix(pathTex, ".png");
        final acq locTex = new acq(resLoc.b(), pathTex);
        return locTex;
    }
    
    public void updateModelTexture(final fuu textureMap, final fkz itemModelGenerator) {
        if (this.texture == null && this.mapTextures == null) {
            return;
        }
        final String[] textures = this.getModelTextures();
        final boolean useTint = this.isUseTint();
        this.bakedModelTexture = makeBakedModel(textureMap, itemModelGenerator, textures, useTint);
        if (this.type == 1 && this.mapTextures != null) {
            final Set<String> keySet = this.mapTextures.keySet();
            for (final String key : keySet) {
                final String tex = this.mapTextures.get(key);
                final String path = StrUtils.removePrefix(key, "texture.");
                if (this.isSubTexture(path)) {
                    final String[] texNames = { tex };
                    final fwr modelTex = makeBakedModel(textureMap, itemModelGenerator, texNames, useTint);
                    if (this.mapBakedModelsTexture == null) {
                        this.mapBakedModelsTexture = new HashMap<String, fwr>();
                    }
                    final String location = invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, path);
                    this.mapBakedModelsTexture.put(location, modelTex);
                }
            }
        }
    }
    
    private boolean isSubTexture(final String path) {
        return path.startsWith("bow") || path.startsWith("crossbow") || path.startsWith("fishing_rod") || path.startsWith("shield");
    }
    
    private boolean isUseTint() {
        return true;
    }
    
    private static fwr makeBakedModel(final fuu textureMap, final fkz itemModelGenerator, final String[] textures, final boolean useTint) {
        final String[] spriteNames = new String[textures.length];
        for (int i = 0; i < spriteNames.length; ++i) {
            final String texture = textures[i];
            spriteNames[i] = StrUtils.removePrefix(texture, "textures/");
        }
        final fkw modelBlockBase = makeModelBlock(spriteNames);
        final fkw modelBlock = itemModelGenerator.a((Function)CustomItemProperties::getSprite, modelBlockBase);
        final fwr model = bakeModel(textureMap, modelBlock, useTint);
        return model;
    }
    
    public static fuv getSprite(final fwu material) {
        final fuu atlasTexture = enn.N().aD().a(material.a());
        return atlasTexture.a(material.b());
    }
    
    private String[] getModelTextures() {
        if (this.type == 1 && this.items.length == 1) {
            final cfu item = cfu.b(this.items[0]);
            final boolean isPotionItem = item == cgc.rv || item == cgc.uu || item == cgc.ux;
            if (isPotionItem && this.damage != null && this.damage.getCountRanges() > 0) {
                final RangeInt range = this.damage.getRange(0);
                final int valDamage = range.getMin();
                final boolean splash = (valDamage & 0x4000) != 0x0;
                final String texOverlay = this.getMapTexture(this.mapTextures, "texture.potion_overlay", "item/potion_overlay");
                String texMain = null;
                if (splash) {
                    texMain = this.getMapTexture(this.mapTextures, "texture.potion_bottle_splash", "item/potion_bottle_splash");
                }
                else {
                    texMain = this.getMapTexture(this.mapTextures, "texture.potion_bottle_drinkable", "item/potion_bottle_drinkable");
                }
                return new String[] { texOverlay, texMain };
            }
            if (item instanceof cdj) {
                final cdj itemArmor = (cdj)item;
                if (itemArmor.d() == cdl.a) {
                    final String material = "leather";
                    String type = "helmet";
                    final bfo equipmentSlot = itemArmor.g();
                    if (equipmentSlot == bfo.f) {
                        type = "helmet";
                    }
                    if (equipmentSlot == bfo.e) {
                        type = "chestplate";
                    }
                    if (equipmentSlot == bfo.d) {
                        type = "leggings";
                    }
                    if (equipmentSlot == bfo.c) {
                        type = "boots";
                    }
                    final String key = invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, material, type);
                    final String texMain2 = this.getMapTexture(this.mapTextures, invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, key), invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, key));
                    final String texOverlay2 = this.getMapTexture(this.mapTextures, invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, key), invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, key));
                    return new String[] { texMain2, texOverlay2 };
                }
            }
        }
        return new String[] { this.texture };
    }
    
    private String getMapTexture(final Map<String, String> map, final String key, final String def) {
        if (map == null) {
            return def;
        }
        final String str = map.get(key);
        if (str == null) {
            return def;
        }
        return str;
    }
    
    private static fkw makeModelBlock(final String[] modelTextures) {
        final StringBuffer sb = new StringBuffer();
        sb.append("{\"parent\": \"builtin/generated\",\"textures\": {");
        for (int i = 0; i < modelTextures.length; ++i) {
            final String modelTexture = modelTextures[i];
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(invokedynamic(makeConcatWithConstants:(ILjava/lang/String;)Ljava/lang/String;, i, modelTexture));
        }
        sb.append("}}");
        final String modelStr = sb.toString();
        final fkw model = fkw.a(modelStr);
        return model;
    }
    
    private static fwr bakeModel(final fuu textureMap, final fkw modelBlockIn, final boolean useTint) {
        final fws modelRotationIn = fws.a;
        final fwu materialParticle = modelBlockIn.c("particle");
        final fuv var4 = materialParticle.c();
        final fxb.a var5 = new fxb.a(modelBlockIn, flb.a, false).a(var4);
        for (final fks blockPart : modelBlockIn.a()) {
            for (final ha direction : blockPart.c.keySet()) {
                fkt blockPartFace = blockPart.c.get(direction);
                if (!useTint) {
                    blockPartFace = new fkt(blockPartFace.b, -1, blockPartFace.d, blockPartFace.e);
                }
                final fwu material = modelBlockIn.c(blockPartFace.d);
                final fuv sprite = material.c();
                final fkr quad = makeBakedQuad(blockPart, blockPartFace, sprite, direction, modelRotationIn);
                if (blockPartFace.b == null) {
                    var5.a(quad);
                }
                else {
                    var5.a(ha.a(modelRotationIn.b().c(), blockPartFace.b), quad);
                }
            }
        }
        return var5.b();
    }
    
    private static fkr makeBakedQuad(final fks blockPart, final fkt blockPartFace, final fuv textureAtlasSprite, final ha enumFacing, final fws modelRotation) {
        final fky faceBakery = new fky();
        return faceBakery.a(blockPart.a, blockPart.b, blockPartFace, textureAtlasSprite, enumFacing, (fwz)modelRotation, blockPart.d, blockPart.e, textureAtlasSprite.getName());
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;)Ljava/lang/String;, this.basePath, this.name, this.type, Config.arrayToString(this.items), this.texture);
    }
    
    public float getTextureWidth(final fuw textureManager) {
        if (this.textureWidth <= 0) {
            if (this.textureLocation != null) {
                final fug tex = textureManager.b(this.textureLocation);
                final int texId = tex.a();
                final int prevTexId = GlStateManager.getBoundTexture();
                GlStateManager._bindTexture(texId);
                this.textureWidth = GL11.glGetTexLevelParameteri(3553, 0, 4096);
                GlStateManager._bindTexture(prevTexId);
            }
            if (this.textureWidth <= 0) {
                this.textureWidth = 16;
            }
        }
        return (float)this.textureWidth;
    }
    
    public float getTextureHeight(final fuw textureManager) {
        if (this.textureHeight <= 0) {
            if (this.textureLocation != null) {
                final fug tex = textureManager.b(this.textureLocation);
                final int texId = tex.a();
                final int prevTexId = GlStateManager.getBoundTexture();
                GlStateManager._bindTexture(texId);
                this.textureHeight = GL11.glGetTexLevelParameteri(3553, 0, 4097);
                GlStateManager._bindTexture(prevTexId);
            }
            if (this.textureHeight <= 0) {
                this.textureHeight = 16;
            }
        }
        return (float)this.textureHeight;
    }
    
    public fwr getBakedModel(final acq modelLocation, final boolean fullModel) {
        fwr bakedModel;
        Map<String, fwr> mapBakedModels;
        if (fullModel) {
            bakedModel = this.bakedModelFull;
            mapBakedModels = this.mapBakedModelsFull;
        }
        else {
            bakedModel = this.bakedModelTexture;
            mapBakedModels = this.mapBakedModelsTexture;
        }
        if (modelLocation != null && mapBakedModels != null) {
            final String modelPath = modelLocation.a();
            final fwr customModel = mapBakedModels.get(modelPath);
            if (customModel != null) {
                return customModel;
            }
        }
        return bakedModel;
    }
    
    public void loadModels(final fww modelBakery) {
        this.modelSpriteLocations = new LinkedHashSet<acq>();
        if (this.model != null) {
            this.loadItemModel(modelBakery, this.model);
        }
        if (this.type == 1 && this.mapModels != null) {
            final Set<String> keySet = this.mapModels.keySet();
            for (final String key : keySet) {
                final String mod = this.mapModels.get(key);
                final String path = StrUtils.removePrefix(key, "model.");
                if (this.isSubTexture(path)) {
                    this.loadItemModel(modelBakery, mod);
                }
            }
        }
    }
    
    public void updateModelsFull() {
        final fwx modelManager = Config.getModelManager();
        final fwr missingModel = modelManager.a();
        if (this.model != null) {
            final acq locItem = getModelLocation(this.model);
            final fwy mrl = new fwy(locItem, "inventory");
            this.bakedModelFull = modelManager.a(mrl);
            if (this.bakedModelFull == missingModel) {
                Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, mrl.a()));
                this.bakedModelFull = null;
            }
        }
        if (this.type == 1 && this.mapModels != null) {
            final Set<String> keySet = this.mapModels.keySet();
            for (final String key : keySet) {
                final String mod = this.mapModels.get(key);
                final String path = StrUtils.removePrefix(key, "model.");
                if (this.isSubTexture(path)) {
                    final acq locItem2 = getModelLocation(mod);
                    final fwy mrl2 = new fwy(locItem2, "inventory");
                    final fwr bm = modelManager.a(mrl2);
                    if (bm == missingModel) {
                        Config.warn(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, mrl2.a()));
                    }
                    else {
                        if (this.mapBakedModelsFull == null) {
                            this.mapBakedModelsFull = new HashMap<String, fwr>();
                        }
                        final String location = invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, path);
                        this.mapBakedModelsFull.put(location, bm);
                    }
                }
            }
        }
    }
    
    private void loadItemModel(final fww modelBakery, final String model) {
        final acq locModel = getModelLocation(model);
        final fwy mrl = new fwy(locModel, "inventory");
        modelBakery.a(mrl);
        final fxc um = modelBakery.a((acq)mrl);
        if (um instanceof fkw) {
            final fkw modelBlock = (fkw)um;
            if (fky.getTextures(modelBlock) != null) {
                for (final Map.Entry<String, Either<fwu, String>> entry : fky.getTextures(modelBlock).entrySet()) {
                    final Either<fwu, String> value = entry.getValue();
                    final Optional<fwu> optionalMaterial = (Optional<fwu>)value.left();
                    if (!optionalMaterial.isPresent()) {
                        continue;
                    }
                    final fwu material = optionalMaterial.get();
                    final acq textureLocation = material.b();
                    this.modelSpriteLocations.add(textureLocation);
                }
            }
        }
    }
    
    private static acq getModelLocation(final String modelName) {
        return new acq(modelName);
    }
}
