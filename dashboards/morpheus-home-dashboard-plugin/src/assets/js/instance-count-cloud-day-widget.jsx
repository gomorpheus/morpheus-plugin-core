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
    //apply state config
    if(props.autoRefresh == false)
      this.state.autoRefresh = false;
    //bind methods
    this.loadData = this.loadData.bind(this);
    this.setData = this.setData.bind(this);
    this.refreshData = this.refreshData.bind(this);
    this.updateChart = this.updateChart.bind(this);
  }

  componentDidMount() {
    this.timeFormat = d3.timeFormat('%-m/%d');
    //configure it
    this.configureChart();
    //load the data
    this.loadData();
    //configure auto refresh
    $(document).on('morpheus:refresh', this.refreshData);
  }

  componentDidUpdate(prevProps, prevState) {
    this.updateChart();
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
    //apply results
    if(results.items) {
      //add the x axis periods
      var axisRow = ['x'];
      var groupSet = [];
      //add results
      if(results.items) {
        for(var index in results.items) {
          var row = results.items[index];
          var rowName = Morpheus.utils.slice(row.name, 25);
          var dataRow = [rowName];
          groupSet.push(rowName);
          //make the xaxis
          for(var valueIndex in row.values) {
            var valueRow = row.values[valueIndex];
            if(index == 0) {
              var axisValue = valueRow[0] // * 1000 - already in milliseconds;
              //console.log('ts - ' + axisValue);
              axisRow.push(axisValue);
              newState.data.totals[valueIndex] = 0;
            }
            var rowItem = parseFloat(valueRow[1]);
            dataRow.push(rowItem);
            newState.data.totals[valueIndex] += rowItem;
            newState.data.total += rowItem;
            if(newState.data.totals[valueIndex] > newState.data.maxValue)
              newState.data.maxValue = newState.data.totals[valueIndex];
          }
          newState.data.items.push(dataRow);
          newState.data.types[rowName] = 'area';
        }
        //add axis row
        newState.data.axisItems.push(axisRow);
        //add groups
        newState.data.groups.push(groupSet);
      }
    }
    //set axis config
    newState.data.maxValue = Morpheus.utils.getNextBaseTen(newState.data.maxValue);
    //set loaded
    newState.loaded = true;
    newState.data.loaded = true;
    newState.date = Date.now();
    newState.error = false;
    newState.errorMessage = null;
    //update the state
    this.setState(newState);
  }

  configureChart() {
    var chartId = this.state.chartId;
    var self = this;
    var chartConfig = {
      bindto: '#instance-count-cloud-day-chart-' + chartId,
      padding: {
        top: 10,
        bottom: 0,
        right: 25
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
    chartConfig.size = { height:140, width:530 };
    //set the legend
    chartConfig.legend = { show:true, align:'left' };
    //set the tooltip
    chartConfig.tooltip = { horizontal:true, contents:(Morph.chartConfigs ? Morph.chartConfigs.tooltip : '') };
    //additional config?
    //generate it
    var widgetChart = c3.generate(chartConfig);
    //store it
    var newState = {};
    newState.chartConfig = chartConfig;
    newState.widgetChart = widgetChart;
    this.setState(newState);
    this.updateChart();
  }

  updateChart() {
    var loaded = this.state.data && this.state.loaded == true;
    //load the data
    if(this.state.data && this.state.loaded == true) {
      var chartId = this.state.chartId;
      var rawData = this.state.data;
      //build up the timeseries data
      var chartData = { 
        columns: [],
        unload: true,
        colors: {}
      };
      //axis items
      if(rawData.axisItems) {
        for(var index in rawData.axisItems) {
          var axisRow = rawData.axisItems[index];
          chartData.columns.push(axisRow);
        }
      }
      //data items
      var seriesCount = 0;
      for(var index in rawData.items) {
        seriesCount++;
        var dataRow = rawData.items[index];
        chartData.columns.push(dataRow);
        chartData.colors[dataRow[0]] = Morph.chartConfigs.colors.chartSetOne[index];
      }
      //add types
      if(rawData.types)
        chartData.types = rawData.types;
      //load history chart
      var widgetChart = this.state.widgetChart;
      //add groups
      if(rawData.groups)
        widgetChart.groups(rawData.groups);
      //update the ranges
      var rangeUpdate = {};
      rangeUpdate.max = {};
      
      //get the max y value
      var maxValue = 0;
      if(rawData.maxValue)
        maxValue = rawData.maxValue;
      rangeUpdate.max.y = maxValue;
      //apply the range
      widgetChart.axis.range(rangeUpdate);
      //set the new data
      widgetChart.load(chartData);
      //refresh it - cuz c3 has bugs on drawing charts
      Morpheus.chartService.flushChart(widgetChart, 400);
      //done
    } else {
      //clear chart data
      var widgetChart = this.state.widgetChart;
      if(widgetChart)
        widgetChart.unload();
    }
  }

  render() {
    //setup
    var showChart = this.state.data && this.state.loaded == true;
    var emptyMessage = this.state.emptyMessage ? this.state.emptyMessage : Morpheus.utils.message('gomorpheus.label.noData');
    //render
    return (
      <div className="widget-container widget-lg">
        <div id={'dashboard-widget-' + this.state.chartId} className="dashboard-widget">
          <div className="dashboard-widget-header">
            <svg className="icon"><use href="/assets/dashboard.svg#provisioning"></use></svg>
            <p>Daily Cloud Instances</p>
          </div>
          <div className="dashboard-widget-body">
            <div id={'instance-count-cloud-day-chart-' + this.state.chartId} className={'stacked-chart stacked-chart-widget' + (showChart ? '' : ' hidden')}></div>
            <div className={'widget-no-data' + (showChart ? ' hidden' : '')}>{emptyMessage}</div>
          </div>
        </div>
      </div>
    );
  }

}

//register it
Morpheus.components.register('instanceCountCloudDayWidget', InstanceCountCloudDayWidget);

$(document).ready(function () {
	const root = ReactDOM.createRoot(document.querySelector('#instance-count-cloud-day-widget'));
	root.render(<InstanceCountCloudDayWidget/>)
});
