// Import stylesheets
import './style.css';
import { Map, TileLayer, LayerGroup, Control, Marker, Icon } from 'leaflet';
import '@geoman-io/leaflet-geoman-free';  
import '@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.css';
import './index.js'

// Write Javascript code!
var map = L.map('map', {
  center: [39.905963, 116.390813],
  zoom: 10,    
  preferCanvas: true
});

const svg = '<svg t="1621166776642" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1407" width="200" height="200"><path d="M512 85.333333c-164.949333 0-298.666667 133.738667-298.666667 298.666667 0 164.949333 298.666667 554.666667 298.666667 554.666667s298.666667-389.717333 298.666667-554.666667c0-164.928-133.717333-298.666667-298.666667-298.666667z m0 448a149.333333 149.333333 0 1 1 0-298.666666 149.333333 149.333333 0 0 1 0 298.666666z" fill="#FF3D00" p-id="1408"></path></svg>'

var greenIcon = L.icon({
  iconUrl: 'data:image/svg+xml,' + encodeURIComponent(svg),
  iconSize: [32,32],
  iconAnchor: [16,32]
});

L.Marker.prototype.options.icon = greenIcon;

var littleton = L.marker([39.8492789996262, 116.331205999857]).bindPopup('铁路卫生学校食堂'),
    denver    = L.marker([39.8496379999938,116.600583999952]).bindPopup('吴记全羊汤'),
    aurora    = L.marker([40.009848, 116.395892]).bindPopup('This is A'),
    golden    = L.marker([39.929931, 116.475494]).bindPopup('This is B'),
    demo      = L.marker([36.929931, 118.475494]).bindPopup('This is demo');
var cities = L.layerGroup([littleton, denver, aurora, golden]);

const amapLayer = new TileLayer(
  'http://wprd0{s}.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=1&style=7',
  {
    subdomains: '1234'
  }
);

const tdtVectorLayer = new TileLayer(
  'http://t0.tianditu.gov.cn/vec_w/wmts?layer=vec&style=default&tilematrixset=w&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&tk=11b55a09c9e0df4a1e91741b455b7f28',
  {}
);

const tdtLabelLayer = new TileLayer(
  'http://t0.tianditu.gov.cn/cva_w/wmts?layer=cva&style=default&tilematrixset=w&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&tk=11b55a09c9e0df4a1e91741b455b7f28',
  {}
);


const tdtLayer = new LayerGroup([tdtVectorLayer, tdtLabelLayer]);

amapLayer.addTo(map);

map.setView([39.909186, 116.397411], 10);

var baseMaps = {
  "高德": amapLayer,
  "天地图": tdtLayer
};

var overlayMaps = {
  "美食": cities
};

var layerControl = L.control.layers(baseMaps, overlayMaps).addTo(map);

// var latlngs = [[38, 116],[42.8511870003798, 117],[40,118.391130999696],[37, 116.5]];
// var polygon = L.polygon(latlngs, {color: 'red'}).addTo(map);

var geoJsonPoint=omnivore.wkt.parse('POINT (116.397411 39.909186)').addTo(map);

// var ciLayer = L.canvasIconLayer({}).addTo(map);
//     var icon = L.icon({
//       iconUrl: 'https://ejuke.github.io/Leaflet.Canvas-Markers/examples/img/pothole.png',
//       iconSize: [20, 18],
//       iconAnchor: [10, 9]
//     });
//     for (var i = 0; i < 100; i++) {
//       var lat = 39.905963 + (Math.random()-Math.random()) * 3;
//       var lng = 116.390813 + (Math.random()-Math.random()) * 3;
//       var marker = L.marker([lat, lng], { icon: icon })
//         .bindPopup("I Am " + i);    //绑定气泡窗口
//       ciLayer.addLayer(marker);
//     }
map.pm.addControls({  
  position: 'topleft',  
  drawCircle: false,  
}); 

var scale = L.control.scale({maxWidth:200,metric:true,imperial:false}).addTo(map);












