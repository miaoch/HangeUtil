package org.other.game.chinesechess;

import java.util.HashMap;
import java.util.Map;

public class Config {
	public static final int DEFAULT_CHAT_PORT = 12345;
	public static final int DEFAULT_GAME_PORT = 12346;
	public static final String DEFAULT_PLAYER_NAME = "无名大侠";
	public static final String DEFAULT_IP_ADDRESS = "127.0.0.1";
	
	public static final int CHESSMAN_LENGTH = 57;
	public static final int GAME_BEGIN_X = 5;
	public static final int GAME_BEGIN_Y = 3;
	
	public static final int TEAM_RED = 1;
	public static final int TEAM_BLACK = 2;
	public static final int COMMAND_GOTO = 1;
	public static final int COMMAND_EAT = 2;
	public static final int COMMAND_JOINGAME = 3;
	
	public static final String RESOURCE_IMAGE_PATH = "resource/image/";
	public static final Map<String, String> IMAGEPATHMAP = new HashMap<String, String>();
	public static final Map<String, String> METHODPREFIXMAP = new HashMap<String, String>();
	public static final Map<Integer, String> COMMANDMAP = new HashMap<Integer, String>();
	public static final Map<Integer, String> TEAMNAMEMAP = new HashMap<Integer, String>();

	static {
		//图片路径
		IMAGEPATHMAP.put("board", RESOURCE_IMAGE_PATH + "board.jpg");
		IMAGEPATHMAP.put("select", RESOURCE_IMAGE_PATH + "select.GIF");
		IMAGEPATHMAP.put("BC", RESOURCE_IMAGE_PATH + "black_che.GIF");
		IMAGEPATHMAP.put("BJ", RESOURCE_IMAGE_PATH + "black_jiang.GIF");
		IMAGEPATHMAP.put("BM", RESOURCE_IMAGE_PATH + "black_ma.GIF");
		IMAGEPATHMAP.put("BP", RESOURCE_IMAGE_PATH + "black_pao.GIF");
		IMAGEPATHMAP.put("BS", RESOURCE_IMAGE_PATH + "black_shi.GIF");
		IMAGEPATHMAP.put("BX", RESOURCE_IMAGE_PATH + "black_xiang.GIF");
		IMAGEPATHMAP.put("BZ", RESOURCE_IMAGE_PATH + "black_zu.GIF");
		IMAGEPATHMAP.put("BCS", RESOURCE_IMAGE_PATH + "black_che_select.GIF");
		IMAGEPATHMAP.put("BJS", RESOURCE_IMAGE_PATH + "black_jiang_select.GIF");
		IMAGEPATHMAP.put("BMS", RESOURCE_IMAGE_PATH + "black_ma_select.GIF");
		IMAGEPATHMAP.put("BPS", RESOURCE_IMAGE_PATH + "black_pao_select.GIF");
		IMAGEPATHMAP.put("BSS", RESOURCE_IMAGE_PATH + "black_shi_select.GIF");
		IMAGEPATHMAP.put("BXS", RESOURCE_IMAGE_PATH + "black_xiang_select.GIF");
		IMAGEPATHMAP.put("BZS", RESOURCE_IMAGE_PATH + "black_zu_select.GIF");
		IMAGEPATHMAP.put("RC", RESOURCE_IMAGE_PATH + "red_che.GIF");
		IMAGEPATHMAP.put("RJ", RESOURCE_IMAGE_PATH + "red_shuai.GIF");
		IMAGEPATHMAP.put("RM", RESOURCE_IMAGE_PATH + "red_ma.GIF");
		IMAGEPATHMAP.put("RP", RESOURCE_IMAGE_PATH + "red_pao.GIF");
		IMAGEPATHMAP.put("RS", RESOURCE_IMAGE_PATH + "red_shi.GIF");
		IMAGEPATHMAP.put("RX", RESOURCE_IMAGE_PATH + "red_xiang.GIF");
		IMAGEPATHMAP.put("RZ", RESOURCE_IMAGE_PATH + "red_bing.GIF");
		IMAGEPATHMAP.put("RCS", RESOURCE_IMAGE_PATH + "red_che_select.GIF");
		IMAGEPATHMAP.put("RJS", RESOURCE_IMAGE_PATH + "red_shuai_select.GIF");
		IMAGEPATHMAP.put("RMS", RESOURCE_IMAGE_PATH + "red_ma_select.GIF");
		IMAGEPATHMAP.put("RPS", RESOURCE_IMAGE_PATH + "red_pao_select.GIF");
		IMAGEPATHMAP.put("RSS", RESOURCE_IMAGE_PATH + "red_shi_select.GIF");
		IMAGEPATHMAP.put("RXS", RESOURCE_IMAGE_PATH + "red_xiang_select.GIF");
		IMAGEPATHMAP.put("RZS", RESOURCE_IMAGE_PATH + "red_bing_select.GIF");
		
		//对应方法前缀 用黑 (兵也用Z来代替 帅也用J来代替)
		METHODPREFIXMAP.put("C", "che");
		METHODPREFIXMAP.put("J", "jiang");
		METHODPREFIXMAP.put("M", "ma");
		METHODPREFIXMAP.put("P", "pao");
		METHODPREFIXMAP.put("S", "shi");
		METHODPREFIXMAP.put("X", "xiang");
		METHODPREFIXMAP.put("Z", "zu");
		
		COMMANDMAP.put(COMMAND_GOTO, "走");
		COMMANDMAP.put(COMMAND_EAT, "吃");
		
		TEAMNAMEMAP.put(TEAM_RED, "红方");
		TEAMNAMEMAP.put(TEAM_BLACK, "黑方");
	}
}
