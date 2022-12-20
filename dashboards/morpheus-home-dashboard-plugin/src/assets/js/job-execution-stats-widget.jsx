/**
 * a summary widget displayrs
 * @author aclement
 */
class JobExecutionStatsWidget extends React.Component {

	constructor(props) {
		super(props);
		//set state
		this.state = {
			loaded: false,
			autoRefresh: true,
			data: null,
			max: 50,
			totalInDb: 50,
			chartId: Morpheus.utils.generateGuid(),
		};
		//apply state config
		if (props.autoRefresh == false)
			this.state.autoRefresh = false;
		//bind methods
		this.setData = this.setData.bind(this);
		this.refreshData = this.refreshData.bind(this);
		this.updateFilterValue = this.updateFilterValue.bind(this);
	}

	componentDidMount() {
		this.loadData();
		$(document).on('morpheus:refresh', this.refreshData);
	}

	//data methods
	refreshData() {
		if (this.state.autoRefresh == true)
			this.loadData();
	}

	loadData() {
		//call api for data...
		var apiFilter;
		var apiOptions = {};
		Morpheus.api.executions.tasks.search('', {max: this.state.max}).then(this.setData)
	}

	setData(results) {
		//set it
		var newState = {};
		newState.data = {};
		newState.data.config = results.config;
		newState.data.meta = results.meta;
		//set the data list
		newState.data.items = results.data;
		//aggregate data for display
		newState.data.successful = results.data.filter((entry) => entry.execution?.status == 'success').length
		newState.data.failed = results.data.filter((entry) => entry.execution?.status == 'error' || entry.execution?.status == 'failed').length
		newState.data.totalResuts = newState.data.successful + newState.data.failed
		newState.data.successPercent = 0
		newState.data.failPercent = 0
		if(newState.data.totalResuts && newState.data.totalResuts != 0) {
			newState.data.successPercent = newState.data.successful / newState.data.totalResuts * 100
			newState.data.failPercent = 100 - newState.data.successPercent
		}

		newState.totalInDb = 0;
		if (results.total)
			newState.totalInDb = results.total;
		newState.data.totalInDb = results.total;

		//mark it loaded
		newState.loaded = true;
		newState.date = Date.now();
		newState.error = false;
		newState.errorMessage = null;
		//update the state
		this.setState(newState);
	}


	updateFilterValue(fieldName, newValue) {
		if(fieldName && newValue) {
			const newState = {};
			newState[fieldName] = newValue;
			console.log({state: this.state, newState})
			this.setState(newState);
			this.loadData();
		}
	}

	renderHeader() {
	    return (<React.Fragment>
	    		<svg className="icon">
					<use href="/assets/navigation/provisioning/executions.svg"></use>
				</svg>
				Job executions ({this.state.max})
				</React.Fragment>)
	  }

	render() {
		const Widget = Morpheus.components.get('Widget');
		const showChart = this.state.data && this.state.loaded == true;
		const emptyMessage = this.state.emptyMessage ? this.state.emptyMessage : Morpheus.utils.message('gomorpheus.label.noData');
		const successfulLabel = Morpheus.utils.message('gomorpheus.successful')
		const failedLabel = Morpheus.utils.message('gomorpheus.failed')
		const successfulCount = this.state.data?.successful ? this.state.data.successful : 0
		const failedCount = this.state.data?.failed ? this.state.data.failed : 0
		const successPercent = this.state.data?.successPercent ? this.state.data.successPercent : 0
		const failPercent = this.state.data?.failPercent ? this.state.data.failPercent : 0
		const filters = {
			update: this.updateFilterValue,
			opts: [{
				label:'max',
				fieldName:'max',
				value: this.state.max,
				type: "number",
				min: 0,
				max: this.state.totalInDb
			}]
		}

		return (
			<Widget settings={filters} title={this.renderHeader()}>
				<div style={{float: 'left', width: '100%'}}>
					<div id={'job-execution-stats-chart-' + this.state.chartId}
						 className={'line-chart-widget' + (showChart ? '' : ' hidden')}
						 style={{position: 'relative', marginTop: '10px'}}>
						<div style={{fontSize: '15px', marginLeft: '7px', marginRight: '12px'}}>
							<span>{successfulLabel}</span><span style={{float: 'right'}}>{successfulCount}</span>
						</div>
						<div id="job-success-line" className="job-success-line"
							 style={{backgroundColor: '#e7e7e6', height: '2px'}}>
							<svg width={successPercent + '%'} height="3px" style={{top: "-12px", position: "relative"}}>
								<line x2="100%" y2="0" strokeWidth="6" stroke={Morph.chartConfigs.colors.green}></line>
							</svg>
						</div>
						<div style={{fontSize: '15px', marginLeft: '7px', marginRight: '12px', marginTop: '20px'}}>
							<span>{failedLabel}</span><span style={{float: 'right'}}>{failedCount}</span>
						</div>
						<div id="job-fail-line" className="job-fail-line"
							 style={{backgroundColor: '#e7e7e6', height: '2px'}}>
							<svg width={failPercent + '%'} height="3px" style={{top: "-12px", position: "relative"}}>
								<line x2="100%" y2="0" strokeWidth="6" stroke={Morph.chartConfigs.colors.red}></line>
							</svg>
						</div>
					</div>
				</div>
			</Widget>
		);
	}
}

//register it
Morpheus.components.register('jobExecutionStatsWidget', JobExecutionStatsWidget);

$(document).ready(function () {
	const root = ReactDOM.createRoot(document.querySelector('#job-execution-stats-widget'));
	root.render(<JobExecutionStatsWidget/>)
});
