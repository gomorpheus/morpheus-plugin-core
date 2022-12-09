/**
 * instance count by status
 * @author bdwheeler
 */
class InstanceCountCloudWidget extends React.Component {
  
  constructor(props) {
    super(props);
    //set state
    this.state = {
      loaded:false,
      autoRefresh:true,
      data:null,
      chartId: Morpheus.utils.generateGuid()
    };
    //apply state config
    if(props.autoRefresh == false)
      this.state.autoRefresh = false;
    //bind methods
    this.setData = this.setData.bind(this);
    this.refreshData = this.refreshData.bind(this);
  }

  componentDidMount() {
    this.configureChart();
    this.loadData();
    $(document).on('morpheus:refresh', this.refreshData);
  }

  //data methods
  refreshData() {
    if(this.state.autoRefresh == true)
      this.loadData();
  }

  loadData() {
    //call api for data...
    var apiFilter;
    var apiOptions = {};
    Morpheus.api.instances.count('group(zoneId:count(id))').then(this.setData);
  }

  setData(results) {
    //set it
    var newState = {};
    newState.data = {};
    newState.data.config = results.config;
    newState.data.meta = results.meta;
    //set the data list
    newState.data.items = results.items;
    //set the count and total
    newState.data.count = 0;
    if(results.count)
      newState.data.count = results.count;
    newState.data.total = 0;
    if(results.total)
      newState.data.total = results.total;
    //mark it loaded
    newState.loaded = true;
    newState.date = Date.now();
    newState.error = false;
    newState.errorMessage = null;
    //update the state
    this.setState(newState);
  }

  componentDidUpdate(prevProps, prevState) {
    this.updateChart();
  }

  configureChart() {
    var chartId = this.state.chartId;
    var self = this;
    //config
    //base config
    var chartConfig = {
      bindto: '#instance-count-cloud-chart-' + chartId,
      data: {
        columns: [],
        type: 'pie',
        colors: {}
      },
      pie: {
        label: {
          show: false
        }
      }
    };
    //set the size
    chartConfig.size = { height:140, width:160 };
    //set the legend
    chartConfig.legend = { show:false };
    //set the tooltip
    chartConfig.tooltip = { contents:Morph.chartConfigs ? Morph.chartConfigs.tooltip : '' };
    //additional config?
    //generate it
    var widgetChart = c3.generate(chartConfig);
    //store it
    var newState = {};
    newState.widgetChart = widgetChart;
    this.setState(newState);
    this.updateChart();
  }

  updateChart() {
    //load the data
    if(this.state.data && this.state.loaded == true) {
      var chartId = this.state.chartId;
      //build up the timeseries data
      var chartData = { 
        columns: [],
        unload: true,
        colors: {}
      };
      for(var index in this.state.data.items) {
        var dataRow = this.state.data.items[index];
        var addRow = [];
        var rowName = dataRow.name.name;
        addRow[0] = rowName;
        addRow[1] = dataRow.value;
        chartData.columns.push(addRow);
        chartData.colors[rowName] = Morph.chartConfigs.statusColor(rowName);
        console.log('status: ' + rowName + ' color: ' + chartData.colors[rowName]);
      }
      //load chart
      var widgetChart = this.state.widgetChart;
      widgetChart.load(chartData);
      //update the title
      var newCount = this.state.data.length ? this.state.data.length : '0';
      $('#dashboard-widget-' + chartId + ' .dashboard-widget-chart-count').text(newCount);
      //$('.c3-chart-arcs-title', $(widgetChart.element)).text(newCount); 
    } else {
      //clear chart data
      var widgetChart = this.state.widgetChart;
      if(widgetChart)
        widgetChart.unload();
    }
  }

  render() {
    var showChart = this.state.data && this.state.loaded == true;
    var emptyMessage = this.state.emptyMessage ? this.state.emptyMessage : Morpheus.utils.message('gomorpheus.label.noData');
    return (
      <div className="widget-container widget-sm">
      <div id={'dashboard-widget-' + this.state.chartId} className="dashboard-widget">
        <div className="dashboard-widget-header">
          <svg className="icon"><use href="/assets/dashboard.svg#provisioning"></use></svg>
          <p>Instances by Cloud</p>
        </div>
        <div className="dashboard-widget-body">
          <div className={'dashboard-widget-chart-count' + (showChart ? '' : ' hidden')} style={{float:'left', width:'30%'}}>
            
          </div>
          <div className="dashboard-widget-chart-body" style={{float:'left', width:'70%'}}>
            <div id={'instance-count-chart-' + this.state.chartId} className={'donut-chart-widget' + (showChart ? '' : ' hidden')} style={{position:'relative'}}></div>
          </div>
          <div className={'widget-no-data' + (showChart ? ' hidden' : '')}>{emptyMessage}</div>
        </div>
      </div>
      </div>
    );
  }

}

//register it
Morpheus.components.register('instanceCountCloudWidget', InstanceCountCloudWidget);

$(document).ready(function () {
	const root = ReactDOM.createRoot(document.querySelector('#instance-count-cloud-widget'));
	root.render(<InstanceCountCloudWidget/>)
});
