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
      data:null
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
    optionSourceService.fetch('clouds', {}, function(zoneResults) {
      var zoneList = zoneResults.data;
      //load instance stats
      var apiFilter;
      var apiOptions = {};
      Morpheus.api.instances.count('group(provisionZoneId:count(id))').then(function(results) {
        if(results.success == true && results.items) {
          var lookupData = { type:'zone', items:zoneList };
          //set zone names
          for(var index in results.items) {
            var row = results.items[index];
            var rowKey = row.name //[0]; //zone id
            var rowName = Morpheus.chart.lookupChartItemName(lookupData, rowKey);
            row.id = rowKey
            row.name = rowName;
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
    newState.data.items = []
    newState.data.total = 0;
    newState.data.count = 0;
    //extract the data
    var chartData = Morpheus.chart.extractNameValueData(results.items, 25, 250, 20);
    newState.data.items = chartData.items;
    newState.data.total = chartData.total;
    newState.data.count = chartData.count;
    //mark loaded
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
    //setup
    var showChart = this.state.data && this.state.loaded == true;
    var countValue = '';
    if(showChart == true)
      countValue = this.state.data.count ? this.state.data.count : '0';
    //render
    return (
      <Widget>
        <WidgetHeader icon="/assets/dashboard.svg#provisioning" title="Instances by Cloud" link="/provisioning/instances"/>
        <div className="dashboard-widget-content">
          <div className={'dashboard-widget-chart-count' + (showChart ? '' : ' hidden')}>
            <span className='count-value'>{countValue}</span>
            <span className='count-label'>clouds</span>
          </div>
          <div className="dashboard-widget-chart-body">
            <PieChartWidget data={this.state.data} config={this.state.chartConfig}/>
          </div>
        </div>
      </Widget>
    );
  }

}

//register it
Morpheus.components.register('instance-count-cloud-widget', InstanceCountCloudWidget);

$(document).ready(function () {
	const root = ReactDOM.createRoot(document.querySelector('#instance-count-cloud-widget'));
	root.render(<InstanceCountCloudWidget/>)
});
