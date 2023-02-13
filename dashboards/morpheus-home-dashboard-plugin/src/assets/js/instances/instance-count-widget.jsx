/**
 * instance count by status
 * @author bdwheeler
 */
class InstanceCountWidget extends React.Component {
  
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
    //call api for data...
    var apiFilter;
    var apiOptions = {};
    Morpheus.api.instances.count('group(status:count(id))').then(this.setData);
  }

  setData(results) {
    //set it
    var newState = {};
    newState.data = {};
    newState.data.config = results.config;
    newState.data.meta = results.meta;
    //set the data list
    var items = [];
    var colors = {};
    if(results.items) {
      for(var index in results.items) {
        var dataRow = results.items[index];
        var addRow = [];
        var rowName = dataRow.name.name;
        if(rowName) {
          addRow[0] = rowName;
          addRow[1] = dataRow.value;
          items.push(addRow);
          colors[rowName] = Morph.chartConfigs.statusColor(rowName);
        }
      }
    }
    newState.data.items = items;
    newState.data.colors = colors;
    //set the count and total
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
    chartConfig.tooltip = { show:true, contents:Morpheus.chart.pieValueTooltip, format:{ title:Morpheus.chart.fixedTooltipTitle('Status') } };
    //done
    return chartConfig;
  }

  render() {
    var showChart = this.state.data && this.state.loaded == true;
    var countValue = '';
    if(showChart == true)
      countValue = this.state.data.total ? this.state.data.total : '0';
    //render it
    return (
      <Widget>
        <WidgetHeader icon="/assets/dashboard.svg#provisioning" title="Instance Status" link="/provisioning/instances"/>
        <div className="dashboard-widget-content">
          <div className={'dashboard-widget-chart-count' + (showChart ? '' : ' hidden')}>
            <span className='count-value'>{countValue}</span>
            <span className='count-label'>instances</span>
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
Morpheus.components.register('instance-count-widget', InstanceCountWidget);

$(document).ready(function () {
	const root = ReactDOM.createRoot(document.querySelector('#instance-count-widget'));
	root.render(<InstanceCountWidget/>)
});
