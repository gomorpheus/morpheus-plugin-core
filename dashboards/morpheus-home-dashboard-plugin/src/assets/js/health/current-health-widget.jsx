/**
 * instance count by status
 * @author bdwheeler
 */
class CurrentHealthWidget extends React.Component {
  
  constructor(props) {
    super(props);
    //set state
    this.state = {
      loaded: false,
      autoRefresh: true,
      data: null
    };
    //apply state config
    if(props.autoRefresh == false)
      this.state.autoRefresh = false;
    //bind methods
    this.loadData = this.loadData.bind(this);
    this.setData = this.setData.bind(this);
    this.refreshData = this.refreshData.bind(this);
  }

  componentDidMount() {
    //load the data
    this.loadData();
    //configure auto refresh
    $(document).on('morpheus:refresh', this.refreshData);
  }

  //data methods
  refreshData() {
    if(this.state.autoRefresh == true)
      this.loadData();
  }

  loadData() {
    var now = new Date();
    var self = this;
    //call api for data..
    Morpheus.api.health.current().then(this.setData);
  }

  setData(results) {
    //data format: [{name:'group by name', values:[ [timestap, value], [] ]}]
    var newState = {};
    newState.data = {};
    //set the data list
    newState.data.success = results.success;
    newState.data.health = results.health;
    //set loaded
    newState.loaded = true;
    newState.data.loaded = true;
    newState.date = Date.now();
    newState.error = false;
    newState.errorMessage = null;
    //update the state
    this.setState(newState);
  }

  render() {
    //setup
    var isLoaded = this.state.data && this.state.data.loaded == true;
    var showChart = isLoaded == true && this.state.data.success == true;
    var healthData = this.state.data && this.state.data.health ? this.state.data.health : {};
    var cpuStatus = healthData.cpu && healthData.cpu.status ? healthData.cpu.status : 'unknown';
    var storageStatus = healthData.storage && healthData.storage.status ? healthData.storage.status : 'unknown';
    var memoryStatus = healthData.memory && healthData.memory.status ? healthData.memory.status : 'unknown';
    var dbStatus = healthData.database && healthData.database.status ? healthData.database.status : 'unknown';
    var queueStatus = healthData.rabbit && healthData.rabbit.status ? healthData.rabbit.status : 'unknown';
    var searchStatus = healthData.elastic && healthData.elastic.status ? healthData.elastic.status : 'unknown';
    if(searchStatus == 'warning' && (healthData.elastic.stats && healthData.elastic.stats.nodeTotal == '1'))
      searchStatus = 'ok';
    //healthData.cpu.status
    //memory
    //database
    //search - status / stats.nodeTotal yellow?
    //queue
    //render
    return (
      <Widget>
        <WidgetHeader icon="/assets/dashboard.svg#provisioning" title={Morpheus.utils.message('gomorpheus.label.systemStatus')} link="/admin/health"/>
        <div className={'dashboard-widget-content' + (showChart ? '' : ' hidden')}>
          <table className="widget-table">
            <tbody>
              <tr>
                <td className="col-xs"><HealthStatusIcon status={cpuStatus}/></td>
                <td>{Morpheus.utils.message('gomorpheus.label.cpu')}</td>
                <td className="col-xs"><HealthStatusIcon status={memoryStatus}/></td>
                <td>{Morpheus.utils.message('gomorpheus.label.memory')}</td>
                <td className="col-xs"><HealthStatusIcon status={storageStatus}/></td>
                <td>{Morpheus.utils.message('gomorpheus.label.storage')}</td>
              </tr>
              <tr>
                <td className="col-xs"><HealthStatusIcon status={dbStatus}/></td>
                <td>{Morpheus.utils.message('gomorpheus.label.database')}</td>
                <td><HealthStatusIcon status={queueStatus}/></td>
                <td>{Morpheus.utils.message('gomorpheus.label.queues')}</td>
                <td><HealthStatusIcon status={searchStatus}/></td>
                <td>{Morpheus.utils.message('gomorpheus.label.search')}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <EmptyWidget isEmpty={isLoaded == true && showChart != true}/>
        <LoadingWidget isLoading={isLoaded != true}/>
      </Widget>
    );
  }

}

//register it
Morpheus.components.register('current-health-widget', CurrentHealthWidget);

$(document).ready(function () {
  const root = ReactDOM.createRoot(document.querySelector('#current-health-widget'));
  root.render(<CurrentHealthWidget/>)
});
