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
    this.state.chartConfig = this.configureChart();
    //apply state config
    if(props.autoRefresh == false)
      this.state.autoRefresh = false;
    //bind methods
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
    var self = this;
    //call api for data..
    var optionSourceService = Morpheus.GlobalOptionSourceService || new Morpheus.OptionSourceService();
    optionSourceService.fetch('clouds', {}, function(results) {
      var zoneList = results.data;
      //load instance stats
      var apiData = [];
      var apiFilter;
      var apiOptions = {};
      Morpheus.api.instances.count('group(provisionZoneId:count(id))').then(function(results) {
        if(results.success == true && results.items) {
          //set zone names
          for(var index in results.items) {
            var row = results.items[index];
            var rowKey = row.name //[0]; //zone id
            var rowZone = Morpheus.data.findNameValueDataById(zoneList, rowKey);
            var rowZoneName = rowZone ? rowZone.name : 'zone-' + rowKey;
            row.id = rowKey
            row.name = rowZoneName;
          }
        }
        self.setData(results);
      });
    });
  }

  setData(results) {
    //set it
    var newState = {};
    newState.data = {};
    newState.data.config = results.config;
    newState.data.meta = results.meta;
    //set the data list
    var items = [];
    if(results.items) {
      for(var index in results.items) {
        var dataRow = results.items[index];
        var addRow = [];
        var rowName = dataRow.name;
        addRow[0] = rowName;
        addRow[1] = dataRow.value;
        items.push(addRow);
      }
    }
    newState.data.items = items;
    //set the count and total
    newState.data.count = 0;
    if(results.count)
      newState.data.count = results.count;
    newState.data.total = 0;
    if(results.total)
      newState.data.total = results.total;
    //mark it loaded
    newState.loaded = true;
    newState.data.loaded = true;
    newState.date = Date.now();
    newState.error = false;
    newState.errorMessage = null;
    //update the state
    this.setState(newState);
  }

  configureChart() {
    var self = this;
    var chartConfig = {
      size: { height: 140, width: 160 },
      legend: { show:false }
    };
    //set the tooltip
    chartConfig.tooltip = { show:true, contents:Morpheus.chart.pieValueTooltip, format:{ title:Morpheus.chart.fixedTooltipTitle('Clouds') } };
    //done
    return chartConfig;
  }

  render() {
    var showChart = this.state.data && this.state.loaded == true;
    var emptyMessage = this.state.emptyMessage ? this.state.emptyMessage : Morpheus.utils.message('gomorpheus.label.noData');
    var countValue = '';
    if(showChart == true)
      countValue = this.state.data.total ? this.state.data.total : '0';
    return (
      <div className="widget-container widget-sm">
      <div id={'dashboard-widget-' + this.state.chartId} className="dashboard-widget">
        <div className="dashboard-widget-header">
          <svg className="icon"><use href="/assets/dashboard.svg#provisioning"></use></svg>
          <p>Instances by Cloud</p>
        </div>
        <div className="dashboard-widget-body">
          <div className={'dashboard-widget-chart-count' + (showChart ? '' : ' hidden')} style={{float:'left', width:'30%'}}>
            <span className='count-value'>{countValue}</span>
            <span className='count-label'>clouds</span>
          </div>
          <div className="dashboard-widget-chart-body" style={{float:'left', width:'70%'}}>
            <PieChartWidget data={this.state.data} config={this.state.chartConfig} emptyMessage=" "/>
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
