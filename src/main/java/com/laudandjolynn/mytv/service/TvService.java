package com.laudandjolynn.mytv.service;

import java.util.List;

import com.laudandjolynn.mytv.event.CrawlEventListener;
import com.laudandjolynn.mytv.model.MyTv;
import com.laudandjolynn.mytv.model.ProgramTable;
import com.laudandjolynn.mytv.model.TvStation;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2015年4月17日 上午9:31:17
 * @copyright: www.laudandjolynn.com
 */
public interface TvService {
	/**
	 * 保存电视台
	 * 
	 * @param stations
	 */
	public void save(TvStation... stations);

	/**
	 * 保存电视节目表
	 * 
	 * @param programTables
	 */
	public void save(ProgramTable... programTables);

	/**
	 * 获取所有电视台分类
	 * 
	 * @return
	 */
	public List<String> getMyTvClassify();

	/**
	 * 获取所有可用于抓取的电视台对象列表
	 * 
	 * @return
	 */
	public List<TvStation> getAllCrawlableStation();

	public TvStation getStation(String stationName);

	/**
	 * 根据显示名称获取电视台对象
	 * 
	 * @param displayName
	 *            电视台显示名
	 * @param classify
	 *            电视台分类
	 * @return
	 */
	public TvStation getStationByDisplayName(String displayName, String classify);

	/**
	 * 根据电视台名称，日期获取电视节目表
	 * 
	 * @param stationName
	 *            电视台名称
	 * @param date
	 *            日期, yyyy-MM-dd
	 * @return
	 */
	public List<ProgramTable> getProgramTable(String stationName, String date);

	/**
	 * 根据电视台分类获取电视台
	 * 
	 * @param classify
	 * @return
	 */
	public List<MyTv> getMyTvByClassify(String classify);

	/**
	 * 判断指定电视台、日期的电视节目表是否已存在
	 * 
	 * @param stationName
	 *            电视台名称
	 * @param date
	 *            日期, yyyy-MM-dd
	 * @return
	 */
	public boolean isProgramTableExists(String stationName, String date);

	/**
	 * 抓取所有电视台
	 * 
	 * @param listeners
	 *            抓取事件监听器
	 * @return
	 */
	public List<TvStation> crawlAllTvStation(CrawlEventListener... listeners);

	/**
	 * 根据名称、日期抓取电视节目表
	 * 
	 * @param tvStation
	 *            电视台对象
	 * @param date
	 *            日期, yyyy-MM-dd
	 * @param listeners
	 *            抓取事件监听器
	 * @return
	 */
	public List<ProgramTable> crawlProgramTable(TvStation tvStation,
			String date, CrawlEventListener... listeners);

}
