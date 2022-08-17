package com.morpheusdata.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * This Utility Class provides a way to determine compare a 'master' list of items to an 'existing' list of items.
 * The comparison results in a list master items that need to be added, a list of items that need to be updated,
 * and a list of existing items that need to be removed.
 *
 * <p><strong>Example:</strong></p>
 * <pre>{@code
 *
 * MatchFunction matchFunction1 = (VirtualImage v, Map m) ->
 * {
 *     return v.id == m.id;
 * };
 *
 * SyncList<VirtualImage,Map> syncList = new SyncList(matchFunction1);
 * SyncListResult syncResult = syncList.buildSyncLists(existingItems, masterItems);
 * System.out.println("Items to add: " + syncResult.addList.size());
 * System.out.println("Items to update: " + syncResult.updateList.size());
 * System.out.println("Items to remove: " + syncResult.removeList.size());
 * }</pre>
 * @author Bob Whiton
 * @param <Existing> The Class Object of the existing items
 * @param <Master> The Class Object of the master items
 */
public class SyncList<Existing, Master> {

	static Logger log = LoggerFactory.getLogger(SyncList.class);

	@FunctionalInterface
	public interface MatchFunction<Existing, Master> {
		boolean compare(Existing a, Master b);
	}

	public static class UpdateItem<Existing, Master> {
		public Existing existingItem;
		public Master masterItem;
		public boolean duplicate = false;
		public Existing duplicateItem;

		public UpdateItem(Existing existingItem, Master masterItem, boolean duplicate) {
			this.existingItem = existingItem;
			this.masterItem = masterItem;
			this.duplicate = duplicate;
		}
	}

	public static class SyncResult<Master, Existing> {
		public Collection<Master> addList = new ArrayList<>();
		public Collection<UpdateItem> updateList = new ArrayList<>();
		public Collection<Existing> removeList = new ArrayList<>();
	}

	private MatchFunction[] matchFunctions;

	public SyncList(MatchFunction... matchFunctions) {
		this.matchFunctions = matchFunctions;
	};

	public SyncResult buildSyncLists(Collection<Existing> existingItems, Collection<Master> masterItems) {
		log.debug("buildSyncLists: existingList:{}, masterList:{}", existingItems, masterItems);
		SyncResult<Master, Existing> result = new SyncResult<>();
		try {
			ArrayList<Master> masterItemsMatchList = new ArrayList<>();
			for(Master mi : masterItems) {
				masterItemsMatchList.add(mi);
			}
			for(Existing existing : existingItems) {
				ArrayList<Master> matches = new ArrayList<>();

				for(MatchFunction mf : this.matchFunctions) {
					if(matches.size() == 0) {
						for (Master mi : masterItemsMatchList) {
							if (mf.compare(existing, mi) == true) {
								matches.add(mi);
							}
						}
					}
				}

				if(matches.size() > 1) {
					int index = 0;
					for(Master match : matches) {
						masterItemsMatchList.remove(masterItemsMatchList.indexOf(match));
						result.updateList.add(new UpdateItem(existing, match, index > 0));
						index++;
					}
				} else if(matches.size() > 0) {
					for(Master match : matches) {
						masterItemsMatchList.remove(masterItemsMatchList.indexOf(match));
						result.updateList.add(new UpdateItem(existing, match, false));
					}
				} else {
					//look for dupe on this size?
					Master dupeMatch = null;
					MatchFunction<Existing, Master> firstMatchFunction = this.matchFunctions[0];
					for(Master mi : masterItems) {
						if(dupeMatch == null) {
							boolean isMatch = firstMatchFunction.compare(existing, mi);
							if(isMatch == true) {
								dupeMatch = mi;
							}
						}
					}

					if(dupeMatch != null) {
						//append the duplicate
						UpdateItem<Existing, Master> existingUpdateItem = null;
						for(UpdateItem<Existing,Master> ui : result.updateList) {
							if(existingUpdateItem == null) {
								if(ui.masterItem == dupeMatch) {
									existingUpdateItem = ui;
								}
							}
						}

						if(existingUpdateItem != null) {
							existingUpdateItem.duplicate = true;
							existingUpdateItem.duplicateItem = existing;
						}
					} else {
						result.removeList.add(existing);
					}
				}
			}
			result.addList = masterItemsMatchList;
		} catch(Exception e) {
			log.error("buildSyncLists error: {}", e);
		}
		return result;
	}
}
