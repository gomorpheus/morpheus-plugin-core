/**
 * group worload count
 * @author bdwheeler
 */
class GroupWorkloadCountWidget extends React.Component {

  constructor(props) {
    //props
    super(props);
    //state
    this.state = { 
      loaded: false,
      autoRefresh: true,
      data: null
    };
    this.state.chartConfig = this.configureChart();
    //refs
    //bind methods
    this.setData = this.setData.bind(this);
    this.loadData = this.loadData.bind(this);
    this.refreshData = this.refreshData.bind(this);
  }

  componentDidMount() {
    //load the data
    this.loadData();
    //auto refresh
    $(document).on('morpheus:refresh', this.refreshData);
    //search selector?
    if(this.props.searchSelector)
      $(document).on('morpheus:list.search' + this.props.searchSelector, this.loadData);
  }

  //data methods
  refreshData() {
    if(this.state.autoRefresh == true)
      this.loadData();
  }

  loadData(filter, options) {
    var self = this;
    //load group names
    var optionSourceService = Morpheus.GlobalOptionSourceService || new Morpheus.OptionSourceService();
    optionSourceService.fetch('groups', {}, function(groupResults) {
      var groupList = groupResults.data;
      //load instance stats
      var apiQuery = 'group(server.provisionSiteId:count(id)) managed = true';
      var apiOptions = { ignoreStatus:true, nodeFormat:'all' };
      Morpheus.api.containers.count(apiQuery, apiOptions).then(function(results) {
        if(results.success == true && results.items) {
          var lookupData = { type:'group', items:groupList };
          //set group names
          for(var index in results.items) {
            var row = results.items[index];
            var rowKey = row.name //[0]; //group id
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

  configureChart() {
    var self = this;
    var chartConfig = { 
      legend: {
        position: 'inset', 
        inset: { anchor:'top-left', x:110, y:0, step:100 }
      },
      size: { height:142, width:360 },
      donut: {
        position: 'left'
      }
    };
    //set the tooltip
    chartConfig.tooltip = { show:true, contents:Morpheus.chart.defaultTooltip, format:{ title:Morpheus.chart.fixedTooltipTitle('Workloads') } };
    //additional config?
    return chartConfig;
  }

  render() {
    //setup
    //render
    return(
      <Widget widgetClass="chart-legend-right">
        <WidgetHeader icon="/assets/infrastructure/sites.svg#Layer_1" title="Group Workloads" link="/infrastructure/groups"/>
        <DonutChartWidget tooltip="morpheus-value" data={this.state.data} config={this.state.chartConfig}/>
      </Widget>
    );
  }

}

//register it
Morpheus.components.register('group-workload-count-widget', GroupWorkloadCountWidget);

$(document).ready(function() {
  const root = ReactDOM.createRoot(document.querySelector('#group-workload-count-widget'));
  root.render(<GroupWorkloadCountWidget/>)
});
