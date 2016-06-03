package me.WeilonYing.CustomisedMinecarts;


/**
 * Stores constants
 * @author Weilon Ying
 *
 */
public class Definitions {
    public static final String SETTING_MAX_MINECART_SPEED = "max_minecart_speed";
    public static final String SETTING_SLOW_WHEN_EMPTY    = "slow_minecart_when_empty";

    //public static final String SETTING_BLOCK_FAST_NAME    = "highspeed_block";
    //public static final String SETTING_BLOCK_FAST_DATA    = "highspeed_block_data";
    //public static final String SETTING_BLOCK_FAST_AMOUNT  = "speed_increase_multiplier";

    public static final String SETTING_BLOCK_FAST       = "highspeed_block";
    public static final String SETTING_BLOCK_SLOW       = "slow_block";
    public static final String SETTING_BLOCK_NORMAL     = "normal_speed_block";
    public static final String SETTING_BLOCK_LAUNCHER   = "launcher_block";

    public static final String SETTING_NAME               = ".name";
    public static final String SETTING_DATA               = ".data";
    public static final String SETTING_AMOUNT             = ".multiplier";

    public static final boolean DEFAULT_SLOW_WHEN_EMPTY   = true;
    public static final double DEFAULT_MAX_MINECART_SPEED_SETTING  = 0.4D;

    public static final String DEFAULT_BLOCK_FAST_NAME    = "BRICK";
    public static final double DEFAULT_BLOCK_FAST_SETTING = 2.0;
    public static final String DEFAULT_BLOCK_SLOW_NAME    = "HARD_CLAY";
    public static final double DEFAULT_BLOCK_SLOW_SETTING = 0.5;
    public static final String DEFAULT_BLOCK_NORMAL_NAME  = "STONE";
    public static final String DEFAULT_BLOCK_LAUNCH_NAME  = "STONE_BRICK";
}
