const http = require('http');
const crypto = require('crypto');
const express = require('express');
const server = express();

server.get('/workloadwexcall', async (req,res) => {
    let rv = '';
    rv = await workloadWithExtCall(rv);
    rv = await workloadWithExtCall(rv);
    rv = await workloadWithExtCall(rv);
    rv = await workloadWithExtCall(rv);
    rv = await workloadWithExtCall(rv);
    rv = await workloadWithExtCall(rv);
    rv = await workloadWithExtCall(rv);
    rv = await workloadWithExtCall(rv);
    rv = await workloadWithExtCall(rv);
    rv = await workloadWithExtCall(rv);
    res.send(rv);
});

server.listen(8080, () => {
    console.log('listening on 8080');
});

let workloadWithExtCall = (prevHash) => new Promise((resolve)=> {
    http.request({hostname:'localhost',method:'GET',port:8088,path:'/'},(response) => {
        response.data = [];
        response.on('data', chunk => {
            response.data.push(chunk);
        });
        response.on('end', () => {
            var databuf = Buffer.concat(response.data);
            resolve(md5('' + databuf + prevHash));
        });
    }).on('error', (error) => {
        console.log("Error: " + error.message);
        resovle('error');
    }).end();
});

function md5(v) {
    const c = crypto.createHmac('md5','0');
    let r = c.update(v).digest('hex');
    return r;
}