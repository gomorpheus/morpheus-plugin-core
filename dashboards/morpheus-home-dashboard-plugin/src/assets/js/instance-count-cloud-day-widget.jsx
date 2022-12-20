/**
 * instanct count by cloud and day
 * @author bdwheeler
 */
class InstanceCountCloudDayWidget extends React.Component {
  
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
    this.loadData = this.loadData.bind(this);
    this.setData = this.setData.bind(this);
    this.refreshData = this.refreshData.bind(this);
  }

  componentDidMount() {
    this.timeFormat = d3.timeFormat('%-m/%d');
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
    var now = new Date();
    var self = this;
    //call api for data..
    var optionSourceService = Morpheus.GlobalOptionSourceService || new Morpheus.OptionSourceService();
    optionSourceService.fetch('clouds', {}, function(results) {
      var zoneList = results.data;
      var apiData = [];
      var apiPromises = [];
      var chartDays = 7;
      //execute for 7 days
      for(var dayCounter = (chartDays - 1); dayCounter >= 0; dayCounter--) {
        //get a day worth of data
        var startDate = new Date();
        startDate.setDate(startDate.getDate() - dayCounter);
        startDate.setHours(0, 0, 0, 0);
        var endDate = new Date(startDate);
        endDate.setDate(endDate.getDate() + 1);
        var apiQuery = 'group(zoneId:count(parentRefId))'
        apiQuery = apiQuery + ' startDate < ' + endDate.toISOString();
        apiQuery = apiQuery + ' and (endDate = null or endDate > ' + startDate.toISOString() + ')';
        var apiOptions = { apiDate:startDate.getTime() };
        var apiPromise = Morpheus.api.usage.count(apiQuery, apiOptions).then(function(results) {
          if(results.success == true && results.items) {
            for(var index in results.items) {
              var row = results.items[index];
              var rowKey = row.name //[0]; //zone id
              var rowValue = row.value //[1]; //count
              var rowZone = Morpheus.data.findNameValueDataById(zoneList, rowKey);
              var rowZoneName = rowZone ? rowZone.name : 'zone-' + rowKey;
              var rowDate = new Date();
              if(results.config.options.apiDate)
                rowDate.setTime(results.config.options.apiDate);
              var dataRow = [rowDate.getTime(), rowValue];
              Morpheus.data.addGroupNameValuesData(apiData, rowZoneName, dataRow);
            }
          }
        });
        apiPromises.push(apiPromise);
      }
      //once all loaded
      $.when.apply($, apiPromises).done(function(results) {
        var newData = { items:apiData };
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

    let data = {
      x:'x',
      columns:[],
      axis:{range:{max:{y:0}}},
      groups:[[]],
      types:{},
      colors:{}
    }
    let maxValue = 0


    if(results.items) {
      //make axis column
      data.columns.push(['x'].concat(results.items[0].values.map(val => val[0])))
      //make value columns
      results.items.forEach((row, index) => {
        let name = Morpheus.utils.slice(row.name, 25);
        let values = row.values.map(val => parseFloat(val[1]))
        let rowMax = Math.max(values)
        rowMax > maxValue? maxValue = rowMax:null
        
        data.groups[0].push(name);
        data.types[name] = 'area';
        data.columns.push([name].concat(values));
        data.colors[name] = Morph.chartConfigs.colors.chartSetOne[index];
      })
      data.axis.range.max.y = maxValue;
    }
    newState.data = data
    newState.data.loaded = true
    newState.loaded = true
    //update the state
    this.setState(newState);
  }

  configureChart() {
    var self = this;
    var chartConfig = {
      padding: {
        top: 10,
        bottom: 0,
        right: 25
      },
      data: {
        x: 'x'
      },
      legend: { 
        show: true,
        align: 'left'
      },
      size: { 
        height:140
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
    //set the tooltip
    chartConfig.tooltip = { show:true, horizontal:true, contents:(Morph.chartConfigs ? Morph.chartConfigs.tooltip : '') };
    //additional config?
    return chartConfig;
  }

  renderHeader() {
    return (<React.Fragment>
      <svg className="icon"><use href="/assets/dashboard.svg#provisioning"></use></svg>
      Daily Cloud Instances
    </React.Fragment>)
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
    let TimeSeriesChart = Morpheus.components.get('TimeSeriesChart');
    return (
      <Widget title={this.renderHeader()}>
        <StackedChartWidget data={this.state.data} config={this.state.chartConfig}/>
        {this.renderNoData()}
      </Widget>
    );
  }

}

//register it
Morpheus.components.register('instanceCountCloudDayWidget', InstanceCountCloudDayWidget);

$(document).ready(function () {
	const root = ReactDOM.createRoot(document.querySelector('#instance-count-cloud-day-widget'));
	root.render(<InstanceCountCloudDayWidget/>)
});
