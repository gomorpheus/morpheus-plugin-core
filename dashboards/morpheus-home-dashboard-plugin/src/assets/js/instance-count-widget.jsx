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
    chartConfig.tooltip = { show:true, contents:Morpheus.chart.pieValueTooltip, format:{ title:Morpheus.chart.fixedTooltipTitle('Status') } };
    //done
    return chartConfig;
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
    // PieChart expects data in this format: {columns:[['name', value],['name2', value2]]}
    let count = 0;
    if(results.count)
      count = results.count;

    let cols = results.items.map (item => {
      return [item.name.name, item.value]
    })
    this.setState({data: {columns:cols, loaded:true}, count:count})
  }


  render() {
    //widgets
    let Widget = Morpheus.components.get('Widget');
    let PieChart = Morpheus.components.get('PieChart');
    //setup
    var emptyMessage = this.state.emptyMessage ? this.state.emptyMessage : Morpheus.utils.message('gomorpheus.label.noData');
    var showChart = this.state.data && this.state.loaded == true;
    //render
    return (
        <Widget>
          <WidgetHeader icon="/assets/dashboard.svg#provisioning" title={Morpheus.utils.message('gomorpheus.label.instanceStatus')}/>
          <div className="flex">
            <div className={'dashboard-widget-chart-count'}>
              <span className='count-value'>{this.state.count}</span>
              <span className='count-label'>instances</span>
            </div>
            <PieChart data={this.state.data} config={this.configureChart()}/>
          </div>
        </Widget>
      )
  }

}

//register it
Morpheus.components.register('dashboard-item-instance-count', InstanceCountWidget);

//register it
// Morpheus.components.register('instanceCountWidget', InstanceCountWidget);

$(document).ready(function () {
	const root = ReactDOM.createRoot(document.querySelector('#instance-count-widget'));
	root.render(<InstanceCountWidget/>)
});
