/**
 * cluster capacity
 * @author bdwheeler
 */
class ClusterCapacityWidget extends React.Component {

  constructor(props) {
    //props
    super(props);
    //state
    this.state = { 
      chartId: Morpheus.utils.generateGuid(),
      autoRefresh: true,
      data: null,
      loaded: false
    };
    //refs
    //bind methods
    this.setData = this.setData.bind(this);
    this.refreshData = this.refreshData.bind(this);
  }

  componentDidMount() {
    //load the data
    this.loadData();
    //auto refresh
    $(document).on('morpheus:refresh', this.refreshData);
  }

  componentDidUpdate() {
    var chartId = '#' + this.state.chartId;
    Morpheus.debounce(this, 500, function() {
      var guageChart = $(chartId);
      Morpheus.chartService.refreshTargetChartLines(guageChart);  
    });
  }

  //data methods
  refreshData() {
    if(this.state.autoRefresh == true)
      this.loadData();
  }

  loadData(filter, options) {
    //load count
    var apiQuery = 'stats(sum(usedMemory),sum(maxMemory),avg(usedCpu),sum(usedStorage),sum(maxStorage)) usedCpu > 1';
    var apiOptions = { serverType:'node' };
    Morpheus.api.servers.count(apiQuery, apiOptions).then(this.setData);
  }

  setData(results) {
    //set it
    var newState = {};
    newState.data = {};
    newState.data.config = results.config;
    newState.data.meta = results.meta;
    //set the data list
    newState.data.items = []
    newState.data.total = 0;
    //extract the data
    var chartData = Morpheus.chart.extractNameValueData(results.items, 25, 100);
    newState.data.items = chartData.items;
    newState.data.total = chartData.total;
    //mark loaded
    newState.loaded = true;
    newState.data.loaded = true;
    newState.date = Date.now();
    newState.error = false;
    newState.errorMessage = null;
    //update the state
    this.setState(newState);
  }

  render() {
    //memory
    var memoryChartTitle = 'n/a';
    var memoryChartLabel = 'memory';
    var memoryPercentUsed = 0;
    var memoryChartColor = 'green';
    //cpu
    var cpuChartTitle = 'n/a';
    var cpuChartLabel = 'cpu';
    var cpuPercentUsed = 0;
    var cpuChartColor = 'green';
    //storage
    var storageChartTitle = 'n/a';
    var storageChartLabel = 'storage';
    var storagePercentUsed = 0;
    var storageChartColor = 'green';
    //if loaded
    if(this.state.data && this.state.data.loaded == true) {
      //memory
      var usedMemory = 0;
      var maxMemory = 0;
      if(this.state.data.items.length > 0)
        usedMemory = this.state.data.items[0].value;
      if(this.state.data.items.length > 1)
        maxMemory = this.state.data.items[1].value;
      if(maxMemory > 0) {
        memoryPercentUsed = (usedMemory / maxMemory * 100);
        memoryChartTitle = Morpheus.utils.formatBytes(usedMemory, 0) + ' of ' + Morpheus.utils.formatBytes(maxMemory);
        //set threshold
        if(memoryPercentUsed > 90)
          memoryChartColor = 'red';
        else if(memoryPercentUsed > 75)
          memoryChartColor = 'yellow';
      }
      //cpu
      if(this.state.data.items.length > 2)
        cpuPercentUsed = this.state.data.items[2].value;
      if(cpuPercentUsed > 0) {
        cpuChartTitle = Math.round(cpuPercentUsed) + '%';
        //set threshold
        if(cpuPercentUsed > 90)
          cpuChartColor = 'red';
        else if(cpuPercentUsed > 75)
          cpuChartColor = 'yellow';
      }
      //storage
      var usedStorage = 0;
      var maxStorage = 0;
      if(this.state.data.items.length > 3)
        usedStorage = this.state.data.items[3].value;
      if(this.state.data.items.length > 4)
        maxStorage = this.state.data.items[4].value;
      if(maxStorage > 0) {
        storagePercentUsed = (usedStorage / maxStorage * 100);
        storageChartTitle = Morpheus.utils.formatBytes(usedStorage, 0) + ' of ' + Morpheus.utils.formatBytes(maxStorage);
        //set threshold
        if(storagePercentUsed > 90)
          storageChartColor = 'red';
        else if(memoryPercentUsed > 75)
          storageChartColor = 'yellow';
      }
    }
    //render it
    return(
      <Widget>
        <WidgetHeader title="Cluster Capacity" icon="/assets/infrastructure/clusters.svg#Layer_1" link="/infrastructure/clusters"/>
        <div className="dashboard-widget-content">
          <div id={this.state.chartId} className="guage-chart-widget">
            <div className={'cpu-guage guage-md progress-bar-' + cpuChartColor} title={cpuChartTitle}>
              <svg viewBox="0 0 100 100" width="100" height="100">
                <circle cx="50" cy="50" r="40" className="chart-bg"/>
                <circle cx="50" cy="50" r="40" className="chart-line" data-value={cpuPercentUsed}/>
                <text x="32" y="60"></text>
              </svg>
              <div className="guage-label">{cpuChartLabel}</div>
            </div>
            <div className={'memory-guage guage-md progress-bar-' + memoryChartColor} title={memoryChartTitle}>
              <svg viewBox="0 0 100 100" width="100" height="100">
                <circle cx="50" cy="50" r="40" className="chart-bg"/>
                <circle cx="50" cy="50" r="40" className="chart-line" data-value={memoryPercentUsed}/>
                <text x="32" y="60"></text>
              </svg>
              <div className="guage-label">{memoryChartLabel}</div>
            </div>
            <div className={'storage-guage guage-md progress-bar-' + storageChartColor} title={storageChartTitle}>
              <svg viewBox="0 0 100 100" width="100" height="100">
                <circle cx="50" cy="50" r="40" className="chart-bg"/>
                <circle cx="50" cy="50" r="40" className="chart-line" data-value={storagePercentUsed}/>
                <text x="32" y="60"></text>
              </svg>
              <div className="guage-label">{storageChartLabel}</div>
            </div>
          </div>
        </div>
      </Widget>
    )
  }

}

//register it
Morpheus.components.register('cluster-capacity-widget', ClusterCapacityWidget);

$(document).ready(function() {
  const root = ReactDOM.createRoot(document.querySelector('#cluster-capacity-widget'));
  root.render(<ClusterCapacityWidget/>)
});
