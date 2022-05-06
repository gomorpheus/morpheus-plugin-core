package com.morpheusdata.request;

import com.morpheusdata.model.ComputeServerInterface;
import com.morpheusdata.model.ServicePlan;
import com.morpheusdata.model.StorageController;
import com.morpheusdata.model.StorageVolume;

import java.util.List;
import java.util.Map;

public class ResizeRequest {

	/**
	 * The desired ServicePlan
	 */
	public ServicePlan plan;

	/**
	 * The desired maxMemory in bytes
	 */
	public Long maxMemory;

	/**
	 * The desired max cores
	 */
	public Long maxCores;

	/**
	 * The desired cores per socket
	 */
	public Long coresPerSocket;

	/**
	 * The desired max storage.
	 */
	public Long maxStorage;

	/**
	 * StorageVolumes that should be deleted
	 */
	public List<StorageVolume> volumesDelete;

	/**
	 * StorageVolumes that should be added
	 */
	public List<Map> volumesAdd;

	/**
	 * StorageVolumes that should be updated
	 */
	public List<UpdateModel<StorageVolume>> volumesUpdate;

	/**
	 * StorageControllers that should be deleted
	 */
	public List<StorageController> controllersDelete;

	/**
	 * StorageControllers that should be added
	 */
	public List<Map> controllersAdd;

	/**
	 * StorageControllers that should be updated
	 */
	public List<UpdateModel<StorageController>> controllersUpdate;

	/**
	 * ComputeServerInterfaces that should be deleted
	 */
	public List<ComputeServerInterface> interfacesDelete;

	/**
	 * ComputeServerInterfaces that should be added
	 */
	public List<Map> interfacesAdd;

	/**
	 * ComputeServerInterfaces that should be updated
	 */
	public List<UpdateModel<ComputeServerInterface>> interfacesUpdate;

	public ResizeRequest(){
	}

	public ResizeRequest(ServicePlan plan,
						 Long maxMemory, Long maxCores, Long maxStorage, Long coresPerSocket,
						 List<StorageVolume> volumesDelete, List<Map> volumesAdd, List<UpdateModel<StorageVolume>> volumesUpdate,
						 List<StorageController> controllersDelete, List<Map> controllersAdd, List<UpdateModel<StorageController>> controllersUpdate,
						 List<ComputeServerInterface> interfacesDelete, List<Map> interfacesAdd, List<UpdateModel<ComputeServerInterface>> interfacesUpdate) {
		this.plan = plan;
		this.maxMemory = maxMemory;
		this.maxCores = maxCores;
		this.coresPerSocket = coresPerSocket;
		this.maxStorage = maxStorage;
		this.volumesDelete = volumesDelete;
		this.volumesAdd = volumesAdd;
		this.volumesUpdate = volumesUpdate;
		this.controllersDelete = controllersDelete;
		this.controllersAdd = controllersAdd;
		this.controllersUpdate = controllersUpdate;
		this.interfacesDelete = interfacesDelete;
		this.interfacesAdd = interfacesAdd;
		this.interfacesUpdate = interfacesUpdate;
	}
}
