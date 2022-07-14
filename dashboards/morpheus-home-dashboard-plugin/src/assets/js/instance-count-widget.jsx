/**
 * a counter widget that loads data
 * @author bdwheeler
 */
class InstanceCountWidget extends React.Component {
  
  constructor(props) {
    super(props);
    //set state
    this.state = { count:0, autoRefresh:true};
  	//apply state config
    if(props.autoRefresh == false)
    	this.state.autoRefresh = false;
  }

  loadData() {
  	//call api for data...
  	//var apiResults = Morpheus.api.call(this.props.apiCall).then(function(apiResults) {
  		//console.log('api results: ' + apiResults);
  	//});
  	//auto reload if enabled
  	if(this.state.autoRefresh) {
  		this.interval = setInterval(() => this.loadData(), 60000);
  	}
  }
  
  componentDidMount() {
  	this.loadData();
  }

  render() {
    return (
      <div className="test">
      	{this.state.count}
      	<div className="test-title">{this.state.title}</div>
      </div>
    );
  }

}

//register it
Morpheus.components.register('instanceCountWidget', KubeClusterMemoryWidget);

$(document).ready(function() {
	ReactDOM.render(
  	<InstanceCountWidget/>,
  	document.querySelector('#instance-count-widget')
	);
});
