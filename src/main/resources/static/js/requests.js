const host = location.hostname;

function getTasks(){
	const promise = axios({
	method:'get',
      url: "/api/tasks",
	});
	return promise.then((response) =>{
		return response.data;
		});
	}

function createTask(title){
	const promise = axios({
	method: 'post',
    url: "/api/tasks",
	data : {
		title : title
		}
	});
	return promise.then((response) =>{
		return response.data;
		});
	}

function updateTask(title, id){
	const promise = axios({
    method: 'put',
    url: "/api/tasks",
    data : {
        id : id,
        title : title
   		}
   	});
	return promise.then((response) =>{
		return response.data;
		});
	}

function deleteAllTasks(){
	const promise = axios({
    method:'delete',
    url: "/api/tasks",
    	});
	return promise.then((response) =>{
		return response.data;
		});
	}

function deleteTask(id){
	const promise = axios({
    method:'delete',
    url: "/api/tasks/" + id,
      	});
	return promise.then((response) =>{
		return response.data;
		});
	}

function logout(){
    const promise = axios({
	method:'get',
	responseType:'document',
    url: "/logout",
	});
	return promise.then((response) =>{
		return response.data;
		});
}