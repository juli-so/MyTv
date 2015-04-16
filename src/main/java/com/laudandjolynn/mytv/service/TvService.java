/*******************************************************************************
 * Copyright 2015 htd0324@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.laudandjolynn.mytv.service;

import java.util.ArrayList;
import java.util.List;

import com.laudandjolynn.mytv.crawler.CrawlerManager;
import com.laudandjolynn.mytv.datasource.TvDao;
import com.laudandjolynn.mytv.datasource.TvDaoImpl;
import com.laudandjolynn.mytv.model.ProgramTable;
import com.laudandjolynn.mytv.model.TvStation;
import com.laudandjolynn.mytv.utils.MemoryCache;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2015年3月30日 下午3:05:47
 * @copyright: www.laudandjolynn.com
 */
public class TvService {
	private TvDao epgDao = new TvDaoImpl();

	/**
	 * 保存电视台
	 * 
	 * @param stations
	 */
	public void save(TvStation... stations) {
		int size = stations == null ? 0 : stations.length;
		if (size == 0) {
			return;
		}

		// 电视台已存在则不再保存
		List<TvStation> resultList = new ArrayList<TvStation>();
		for (int i = 0; i < size; i++) {
			TvStation station = stations[i];
			String stationName = station.getName();
			if (isStationExists(stationName)) {
				continue;
			}
			resultList.add(station);
		}

		int rsize = resultList.size();
		if (rsize > 0) {
			stations = new TvStation[rsize];
			stations = resultList.toArray(stations);
			// query from db
			boolean[] result = epgDao.isStationExists(stations);
			resultList.clear();
			for (int i = 0; i < result.length; i++) {
				if (!result[i]) {
					resultList.add(stations[i]);
				}
			}
			rsize = resultList.size();
			if (rsize > 0) {
				stations = new TvStation[rsize];
				epgDao.save(resultList.toArray(stations));
			}
		}
	}

	/**
	 * 判断电视台是否存在
	 * 
	 * @param stationName
	 * @return
	 */
	public boolean isStationExists(String stationName) {
		return MemoryCache.getInstance().isStationExists(stationName)
				|| epgDao.isStationExists(stationName);
	}

	/**
	 * 保存电视台节目表
	 * 
	 * @param programTables
	 */
	public void save(ProgramTable... programTables) {
		int size = programTables == null ? 0 : programTables.length;
		if (size == 0) {
			return;
		}
		List<ProgramTable> resultList = new ArrayList<ProgramTable>();
		for (int i = 0; i < size; i++) {
			ProgramTable pt = programTables[i];
			String stationName = pt.getStationName();
			String date = pt.getAirDate();
			if (!isStationExists(stationName)
					|| epgDao.isProgramTableExists(stationName, date)) {
				continue;
			}
			resultList.add(pt);
		}
		programTables = new ProgramTable[resultList.size()];
		epgDao.save(resultList.toArray(programTables));
	}

	/**
	 * 获取电视台分类
	 * 
	 * @return
	 */
	public List<String> getTvStationClassify() {
		return epgDao.getTvStationClassify();
	}

	/**
	 * 获取所有电视台列表
	 * 
	 * @return
	 */
	public List<TvStation> getAllStation() {
		return epgDao.getAllStation();
	}

	/**
	 * 根据名称获取电视台对象
	 * 
	 * @param stationName
	 * @return
	 */
	public TvStation getStation(String stationName) {
		TvStation station = MemoryCache.getInstance().getStation(stationName);
		if (station == null) {
			station = epgDao.getStation(stationName);
			MemoryCache.getInstance().addCache(station);
		}
		return station;
	}

	/**
	 * 根据显示名取得电视台对象
	 * 
	 * @param displayName
	 * @param classify
	 * @return
	 */
	public TvStation getStationByDisplayName(String displayName, String classify) {
		return epgDao.getStationByDisplayName(displayName, classify);
	}

	/**
	 * 获取指定电视台节目表
	 * 
	 * @param stationName
	 * @param date
	 * @return
	 */
	public List<ProgramTable> getProgramTable(String stationName, String date) {
		return epgDao.getProgramTable(stationName, date);
	}

	/**
	 * 根据电视台分类查询分类下的所有电视台
	 * 
	 * @param classify
	 * @return
	 */
	public List<TvStation> getTvStationByClassify(String classify) {
		return epgDao.getTvStationByClassify(classify);
	}

	/**
	 * 判断指定的电视节目表是否已存在
	 * 
	 * @param stationName
	 * @param date
	 * @return
	 */
	public boolean isProgramTableExists(String stationName, String date) {
		return epgDao.isProgramTableExists(stationName, date);
	}

	/**
	 * 根据电视台名称、日期抓取电视节目表
	 * 
	 * @param stationName
	 *            电视台名称
	 * @param date
	 *            日期,yyyy-MM-dd
	 * @return
	 */
	public List<ProgramTable> crawlAllProgramTable(String stationName,
			String date) {
		List<ProgramTable> ptList = CrawlerManager.getInstance().getCrawler()
				.crawlProgramTable(stationName, date);
		ProgramTable[] ptArray = new ProgramTable[ptList.size()];
		save(ptList.toArray(ptArray));
		return ptList;
	}

	/**
	 * 根据日期抓取所有电视节目表
	 * 
	 * @param date
	 * @return
	 */
	public List<ProgramTable> crawlAllProgramTable(String date) {
		List<ProgramTable> ptList = CrawlerManager.getInstance().getCrawler()
				.crawlAllProgramTable(date);
		ProgramTable[] ptArray = new ProgramTable[ptList.size()];
		save(ptList.toArray(ptArray));
		return ptList;
	}

	/**
	 * 抓取所有电视台
	 * 
	 * @return
	 */
	public List<TvStation> crawlAllTvStation() {
		List<TvStation> stationList = CrawlerManager.getInstance().getCrawler()
				.crawlAllTvStation();
		// 写数据到tv_station表
		TvStation[] stationArray = new TvStation[stationList.size()];
		save(stationList.toArray(stationArray));
		return stationList;
	}
}
