/**
 * cluster worload count
 * @author bdwheeler
 */
class ClusterWorkloadCountWidget extends React.Component {

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
    this.onLegendClick = this.onLegendClick.bind(this);
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
    //load count
    var apiQuery = 'group(resourcePool.serverGroup.name:count(id)) managed = true';
    var apiOptions = { ignoreStatus:true };
    //apply search config
    if(this.props.searchSelector)
      Morpheus.api.applySearchData(this.props.searchSelector, apiOptions);
    //execute search
    Morpheus.api.containers.count(apiQuery, apiOptions).then(this.setData);
  }

  setData(results) {
    //set it
    var newState = {};
    newState.data = {};
    newState.data.config = results.config;
    newState.data.meta = results.meta;
    //set the data list
    newState.data.items = [];
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

  onLegendClick(chart) {
    var total = 0;
    for(const item of chart.data()) {
      if(chart.internal.isTargetToShow(item.id)){
        total += item.values[0].value;
      }
    }
    $('.c3-chart-arcs-title', $(chart.element)).text(total);
  }

  configureChart() {
    var self = this;
    var chartConfig = { 
      legend: {
        show: true,
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
        <WidgetHeader title="Cluster Workloads" icon="/assets/infrastructure/clusters.svg#Layer_1" title="Cluster Workloads" link="/infrastructure/clusters"/>
        <DonutChartWidget data={this.state.data} config={this.state.chartConfig} onLegendClick={this.onLegendClick}/>
      </Widget>
    );
  }

}

//register it
Morpheus.components.register('cluster-workload-count-widget', ClusterWorkloadCountWidget);

$(document).ready(function() {
  const root = ReactDOM.createRoot(document.querySelector('#cluster-workload-count-widget'));
  root.render(<ClusterWorkloadCountWidget/>)
});
