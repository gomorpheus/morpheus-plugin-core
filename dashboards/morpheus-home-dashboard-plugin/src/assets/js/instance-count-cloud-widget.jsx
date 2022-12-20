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
    //apply state config
    if(props.autoRefresh == false)
      this.state.autoRefresh = false;
    //bind methods
    this.setData = this.setData.bind(this);
    this.refreshData = this.refreshData.bind(this);
  }

  componentDidMount() {
    this.loadData();
    //configure auto refresh
    $(document).on('morpheus:refresh', this.refreshData);
  }

  componentWillUnmount() {
    $(document).off('morpheus:refresh', this.refreshData);
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

  //data methods
  refreshData() {
    if(this.state.autoRefresh == true)
      this.loadData();
  }

  loadData() {
    var self = this;
    //call api for data..
    var optionSourceService = Morpheus.GlobalOptionSourceService || new Morpheus.OptionSourceService();
    optionSourceService.fetch('clouds', {}, (zonesResult) => {
      var zoneList = zonesResult.data;
      //load instance stats
      var apiData = [];
      var apiFilter;
      var apiOptions = {};
      Morpheus.api.instances.count('group(provisionZoneId:count(id))').then((results) => {
        if(results.success == true && results.items) {
          //set zone names
          this.setData(zoneList, results)
          
        }
      });
    });
  }

  setData(zoneList, results) {
    let items = results.items.map( row => {
      var rowZone = Morpheus.data.findNameValueDataById(zoneList, row.name);
      row.id = row.name
      row.name = rowZone ? rowZone.name : 'zone-' + row.name
      return [row.name, row.value]
    });
    let count = results.count || 0

    this.setState({
      loaded: true,
      date: Date.now(),
      error: false,
      data: {columns:items},
      count:count
    })
  }

  renderHeader() {
    return (<React.Fragment><svg className="icon"><use href="/assets/dashboard.svg#provisioning"></use></svg>Instance By Cloud</React.Fragment>)
  }
  renderNoData() {
    var showChart = this.state.data && this.state.loaded == true;
    var emptyMessage = this.state.emptyMessage ? this.state.emptyMessage : Morpheus.utils.message('gomorpheus.label.noData');
    if (!showChart) {
      return (<div className={'widget-no-data'}>{emptyMessage}</div>)
    }
  }

  render() {
    let Widget = Morpheus.components.get('Widget');
    let PieChart = Morpheus.components.get('PieChart');
    return (
      <Widget title={this.renderHeader()}>
        <div className="flex">
          <div className={'dashboard-widget-chart-count'}>
            <span className='count-value'>{this.state.count}</span>
            <span className='count-label'>clouds</span>
          </div>
          <PieChart data={this.state.data} config={this.configureChart()}/>
        </div>
        {this.renderNoData()}
      </Widget>
    );
  }

}

//register it
Morpheus.components.register('instanceCountCloudWidget', InstanceCountCloudWidget);

$(document).ready(function () {
	const root = ReactDOM.createRoot(document.querySelector('#instance-count-cloud-widget'));
	root.render(<InstanceCountCloudWidget/>)
});
