$(function() {
  chartL1();
  var intervalId = setInterval(function() {
    console.log("定时任务执行了！");
    chartL1();
  }, 60000)
})
let data
function chartL1() {
  get(host+"/admin/monitor",function (data1){
    console.log(data1)
    data=data1

    let cpu1;
    let internal1;
    let disc1;
    let downFlag1 = data.serverDtoList[0].downFlag;
    if(downFlag1 == 0){
      cpu1 = 100;
      internal1 = 100
      disc1 =100;
    }else {
      cpu1 = data.serverDtoList[0].cpu;
      internal1 = data.serverDtoList[0].internal;
      disc1 = data.serverDtoList[0].disc;
      $("#hadoop101ZK").text(data.serverDtoList[0].zk)
    }
    let options1=setOptions('hadoop101',cpu1,internal1,disc1,downFlag1)
    var hadoop101 = new ApexCharts(
        document.querySelector("#hadoop101"),
        options1
    );
    let cpu2 ;
    let internal2;
    let disc2;
    let downFlag2 = data.serverDtoList[1].downFlag;
    if (downFlag2 == 0){
     cpu2 = 100;
     internal2 = 100;
     disc2 = 100;
    }else {
      cpu2 = data.serverDtoList[1].cpu;
      internal2 = data.serverDtoList[1].internal;
      disc2 = data.serverDtoList[1].disc;
      $("#hadoop102ZK").text(data.serverDtoList[1].zk)
    }
    let options2=setOptions('hadoop102',cpu2,internal2,disc2,downFlag2)
    var hadoop102= new ApexCharts(
        document.querySelector("#hadoop102"),
        options2
    );
    let cpu3;
    let internal3;
    let disc3;
    let downFlag3 = data.serverDtoList[2].downFlag;
    if(downFlag3 ==0) {
      cpu3 = 100;
      internal3 = 100;
      disc3 = 100;
    }else {
      cpu3 = data.serverDtoList[2].cpu;
      internal3 = data.serverDtoList[2].internal;
      disc3 = data.serverDtoList[2].disc;
      $("#hadoop103ZK").text(data.serverDtoList[2].zk)
    }
    let options3 = setOptions('hadoop103',cpu3,internal3,disc3,downFlag3)
    var hadoop103 = new ApexCharts(
        document.querySelector("#hadoop103"),
        options3
    );

    hadoop101.render();
    hadoop102.render();
    hadoop103.render();
  })
}

/**
 *
 * @param hadoopName 集群名称
 * @param cpu cpu
 * @param internal 内存
 * @param disc 磁盘
 * @param downFlag 是否宕机
 */
function setOptions(hadoopName,cpu,internal,disc,downFlag) {
  let cpuColor = '#38e710'
  let internalColor = '#fde911'
  let discColor = '#4166ea'
  let options = {
    chart: {
      height: 350,
      type: 'radialBar',
      toolbar: {
        show: false
      },
      zoom: {
        enabled: false
      },
      fontFamily: 'Poppins, sans-serif',
    },
    plotOptions: {
      radialBar: {
        dataLabels: {
          name: {
            fontSize: '28px',
          },
          value: {
            fontSize: '18px',
          },
          total: {
            show: true,
            label: hadoopName,
            formatter: function(w) {
              let message = '';
              if(downFlag == 1) {
                let cpuMessage = '';
                let internalMessage = '';
                let discMessage = '';
                if (cpu > 80) {
                  cpuMessage = 'CPU已使用' + cpu + '%\n';
                }
                if (internal > 80) {
                  internalMessage = '内存不足' + Number(100 - internal).toFixed() + '%\n';
                }
                if (disc > 80) {
                  discMessage = '磁盘可用空间不足' + Number(100 - disc).toFixed() + "%";
                }
                if (cpuMessage !== '' || internalMessage !== '' || discMessage !== '') {
                  message = cpuMessage + internalMessage + discMessage;
                } else {
                  message = '正常'
                }
              } else {
                message = "已宕机"
              }
              return message
            }
          }
        }
      }
    },
    series: [disc, internal, cpu],
    labels: ['磁盘', '内存', 'cpu'],
    colors: [colorChange(disc,discColor), colorChange(internal,internalColor), colorChange(cpu,cpuColor)]
  }
  return options;
}

function colorChange(number,color){
  if(number>85){
    return '#ea0a0a'
  }
  return color;
}