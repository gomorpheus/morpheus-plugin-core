/**
 * instanct count by cloud and day
 * @author bdwheeler
 */
class InstanceCountCloudDayWidget extends React.Component {
  
  constructor(props) {
    super(props);
    //set state
    this.state = {
      loaded: false,
      autoRefresh: true,
      data: null,
      rangeType:'days',
      rangeCount: 7
    };
    this.state.chartConfig = this.configureChart();
    //apply state config
    if(props.autoRefresh == false)
      this.state.autoRefresh = false;
    //bind methods
    this.loadData = this.loadData.bind(this);
    this.setData = this.setData.bind(this);
    this.refreshData = this.refreshData.bind(this);
    this.onPillChange = this.onPillChange.bind(this);
  }

  componentDidMount() {
    this.timeFormat = d3.timeFormat('%-m/%d');
    //load the data
    this.loadData();
    //configure auto refresh
    $(document).on('morpheus:refresh', this.refreshData);
  }

  componentDidUpdate(prevProps,prevState) {
    if(prevState.rangeCount !== this.state.rangeCount) {
      this.loadData()
    }
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
    var optionSourceService = Morpheus.GlobalOptionSourceService || new Morpheus.OptionSourceService();
    optionSourceService.fetch('clouds', {}, function(cloudResults) {
      var zoneList = cloudResults.data;
      var newData = Morpheus.chart.getBlankTimeseriesData();
      var chartRange = self.state.rangeType;
      var chartCount = self.state.rangeCount;
      var chartDays = chartRange == 'days' ? chartCount : (chartCount * 7);
      //execute for 7 days
      var startDate = new Date();
      startDate.setDate(startDate.getDate() - (chartDays - 1));
      startDate.setHours(0, 0, 0, 0);
      var apiQuery = 'group(zoneId:count(parentRefId))';
      var apiOptions = { 'range.startDate':startDate.toISOString(), 'range.type':chartRange, 'range.count':chartCount, max:1000 };
      //execute it
      Morpheus.api.usage.count(apiQuery, apiOptions).then(function(results) {
        if(results.success == true && results.items) {
          var lookupData = { type:'zone', items:zoneList };
          newData = Morpheus.chart.apiDataToTimeseriesData(results.items, lookupData, 25, 25, 10);
        }
        //set the data
        self.setData(newData);
      });
    });
  }

  setData(results) {
    //data format: [{name:'group by name', values:[ [timestap, value], [] ]}]
    var newState = {};
    newState.data = {};
    //set the data list
    newState.data.axisItems = [];
    newState.data.items = [];
    newState.data.groups = [];
    newState.data.types = {};
    newState.data.total = 0;
    newState.data.maxValue = 0;
    newState.data.totals = [];
    //extract the data
    newState.data.items = results.items;
    newState.data.total = results.total;
    newState.data.totals = results.totals;
    newState.data.types = results.types;
    newState.data.axisItems.push(results.dateItems);
    newState.data.groups.push(results.groupItems);
    newState.data.maxValue = Morpheus.utils.getNextBaseTen(results.maxValue);
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
      padding: {
        top: 5,
        bottom: 0,
        right: 20
      },
      data: {
        x: 'x',
        columns: [],
        colors: {}
      },
      axis: {
        x: {
          type: 'timeseries',
          tick: {
            fit: false,
            outer: false,
            count: 7,
            format: function(x) { // x comes in as a time string.
              return self.timeFormat(x);
            }
          },
          padding: { left: 0, right:0 }
        },
        y: {
          min: 0,
          max: 10,
          tick: {
            count: 6
          },
          padding: { top:0, bottom:0 }
        }
      }
    };
    //set the size
    chartConfig.size = { height:130, width:540 };
    //set the legend
    chartConfig.legend = { show:false, align:'left' };
    //set the tooltip
    chartConfig.tooltip = { show:true, horizontal:true, contents:(Morph.chartConfigs ? Morph.chartConfigs.tooltip : '') };
    //additional config?
    return chartConfig;
  }

  onPillChange(value) {
    var newState = {};
    newState.rangeType = value;
    newState.rangeCount = newState.rangeType == 'days' ? 7 : 5; //5 weeks
    this.setState(newState, this.loadData);
  }

  render() {
    //setup
    var pillList = [
      {name:'Week', value:'days'},
      {name:'Month', value:'weeks'}
    ];
    //render
    return (
      <Widget>
        <WidgetHeader icon="/assets/dashboard.svg#provisioning" title="Daily Cloud Instances" link="/provisioning/instances"/>
        <WidgetPills pills={pillList} defaultValue={this.state.rangeType} align="center" onPillChange={this.onPillChange}/>
        <StackedChartWidget data={this.state.data} config={this.state.chartConfig}/>
      </Widget>
    );
  }

}

//register it
Morpheus.components.register('instance-count-cloud-day-widget', InstanceCountCloudDayWidget);

$(document).ready(function () {
	const root = ReactDOM.createRoot(document.querySelector('#instance-count-cloud-day-widget'));
	root.render(<InstanceCountCloudDayWidget/>)
});
