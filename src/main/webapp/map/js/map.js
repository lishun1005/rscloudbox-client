//得到地图的全局参数
$(document).ready(function(e) {
	makeMapContent();
})
var map, wfst,featurelayer;
var selectstyle;
var drawPolygon,Square;
var WGS84 = new OpenLayers.Projection("EPSG:4326");
var wkt_c = new OpenLayers.Format.WKT();
function getMapGlobalParams() {
	var url, unit, resolutions, initiExtentStr;
	var MapGlobalParams = g_mapParams.mv_ne_sw_world_china_0public_Layer2013;
	url = MapGlobalParams.url;
	resolutions = MapGlobalParams.resolutions;
	initiExtentStr = MapGlobalParams.strExtent;
	proj = MapGlobalParams.strProj;
	unit = MapGlobalParams.units;
	return {
		proj : proj,
		url : url,
		unit : unit,
		resolutions : resolutions,
		initiExtentStr : initiExtentStr
	};
}
// 获取map选项参数options
function getMapOptions() {
	var options = null;
	var params = getMapGlobalParams();
	var unit, resolutions;
	unit = params.unit;
	resolutions = params.resolutions;
	proj = params.proj;
	options = {
		unit : unit,
		projection : proj,
		displayProjection : WGS84,
		resolutions : resolutions,
		controls : [
				new OpenLayers.Control.Navigation(),
				new OpenLayers.Control.ChinarsPanZoomBar({
					position : new OpenLayers.Pixel(2, 10)
				}),
				new OpenLayers.Control.LayerSwitcher(),
				new OpenLayers.Control.MousePosition(
						{
							prefix : 'Data © <a target="_blank" href=http://www.navinfo.com/>四维图新</a> & <a target="_blank" href=http://www.rsclouds.com>广东中科遥感</a>  坐标点信息: ',
							separator : ' | ',
							numDigits : 6
						}) 
				],
		restrictedExtent: new OpenLayers.Bounds(-180.0,-90.0,180,90.0)
	};
	return options;
}
function makeMapContent() {
	var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;
	renderer = (renderer) ? [ renderer ]
			: OpenLayers.Layer.Vector.prototype.renderers;
	if (map)
		map.destroy();
	var mapOptions = getMapOptions();
	if (mapOptions) {
		map = new OpenLayers.Map("map", mapOptions);
	} else {
		map = new OpenLayers.Map("map");
	}
	var baselayerSwWold = g_mapParams.mv_ne_sw_world_china_0public_Layer2013;
	var baselayerSwWoldlayer = new OpenLayers.Layer.WMS(baselayerSwWold.name,
			baselayerSwWold.url, {
				layers : baselayerSwWold.layers,
				format : baselayerSwWold.format
			}, {
				//wrapDateLine: true,
				transitionEffect : true,
				resolutions : baselayerSwWold.resolutions,
				serverResolutions : baselayerSwWold.resolutions,
				isBaseLayer : baselayerSwWold.isBaseLayer,
				//displayInLayerSwitcher: false,
				tileSize : new OpenLayers.Size(256, 256),
				tileOrigin : new OpenLayers.LonLat(-180.0, 90.0)
			});
	/*var baselayerSwChina = g_mapParams.mv_sw_china_0public_Layer2013;
	var baselayerSwChinalayer = new OpenLayers.Layer.WMS(baselayerSwChina.name,
			baselayerSwChina.url, {
				layers : baselayerSwChina.layers,
				format : baselayerSwChina.format
			}, {
				transitionEffect : true,
				resolutions : baselayerSwChina.resolutions,
				serverResolutions : baselayerSwChina.resolutions,
				isBaseLayer : baselayerSwChina.isBaseLayer,
				// displayInLayerSwitcher: false,
				tileSize : new OpenLayers.Size(256, 256),
				tileOrigin : new OpenLayers.LonLat(-180.0, 90.0)
			});*/
	wfst = new OpenLayers.Layer.Vector("drawExtent", {
		styleMap : new OpenLayers.StyleMap({
			"default" : new OpenLayers.Style({
				strokeColor : "#bb0000",
				strokeOpacity : 0.3,
				fillColor : "#993300",
				fillOpacity : 0.1
			})
			
		}),
		displayInLayerSwitcher:false
	});
	featurelayer = new OpenLayers.Layer.Vector("drawExtent", {
		styleMap : new OpenLayers.StyleMap({
			"default" : new OpenLayers.Style({
				strokeColor : "#bb0000",
				strokeOpacity : 0.3,
				fillColor : "#993300",
				fillOpacity : 0.1
			}),
			 "select": new OpenLayers.Style({
				strokeColor : "#BB00FF",
				strokeOpacity : 0.3,
				fillColor : "#FFEE00",
				fillOpacity :0.1
			 })
		}),
		displayInLayerSwitcher:false
	});
	
	/*map.addLayers([ baselayerSwWoldlayer, baselayerSwChinalayer, wfst,featurelayer]);*/
	map.addLayers([ baselayerSwWoldlayer,  wfst,featurelayer]);
	//map.addLayers([ baselayerSwWoldlayer]);
	
	selectControl = new OpenLayers.Control.SelectFeature(
			featurelayer,{
				clickout: false, 
				toggle: true,
				multiple: true, 
				hover: false,
				onSelect: onFeatureSelect, 
				onUnselect: onFeatureUnselect,
				toggleKey: "ctrlKey", // ctrl key removes from selection
				multipleKey: "shiftKey" // shift key adds to selection
			}
		);
	map.addControl(selectControl);
	selectControl.activate();
	if (typeof(selectControl.handlers) != "undefined") { // OL 2.7
		selectControl.handlers.feature.stopDown = false;
		}else if (typeof(selectControl.handler) != "undefined") { // OL < 2.7
			selectControl.handler.stopDown = false;
			selectControl.handler.stopUp = false;
		}
	
	map.zoomToExtent(new OpenLayers.Bounds(70.40650628256762,
			-0.9954432050011515, 138.18927647840744, 56.16093056737958));

	 drawPolygon = new OpenLayers.Control.DrawFeature(wfst,
			OpenLayers.Handler.Polygon, {
				title : "画面",
				displayClass : "olControlDrawFeaturePolygon",
				multi : true
			});
	 Square = new OpenLayers.Control.DrawFeature(wfst,
			OpenLayers.Handler.RegularPolygon, {
				title : "画矩形",
				displayClass : "olControlDrawFeatureSquarePolygon",
				handlerOptions : {
					sides : 4,
					irregular : true
				}
			});
	wfst.events.register("beforefeatureadded", drawPolygon, clearFeatureDrawPolygon);
	wfst.events.register("featureadded", drawPolygon, redrawFeatureDrawPolygon);
	drawPolygon.events.on({
		"activate" : activeDrawPolygon,
		"deactivate" : deactivateDrawPolygon
	});
	var container = document.getElementById("graph_choose");
	var panel = new OpenLayers.Control.Panel({
		'displayClass' : 'customEditingToolbar',
		div : container,
		title : "控件组1"
	});
	map.addControl(panel);
	panel.addControls([Square ,drawPolygon]);
	map.events.on({
	    "moveend" : updateMapMoveend
	});
}
function onFeatureSelect(e){
	var dataid = e.fid;
	$("#"+dataid).addClass("active-blue");
	$("#"+dataid).find(".ite").addClass("gxuan");
	$("#"+dataid).find(".square-checkbox").addClass("checkbox-blue");
	originalimageresultListgxuanitem();
	orthoimageresultListgxuanitem();
}
function onFeatureUnselect(e){
	var dataid = e.fid;
	$("#"+dataid).removeClass("active-blue");
	$("#"+dataid).find(".ite").removeClass("gxuan");
	$("#"+dataid).find(".square-checkbox").removeClass("checkbox-blue");
	originalimageresultListgxuanitem();
	orthoimageresultListgxuanitem();
}
function activeDrawPolygon(e) {
}
function deactivateDrawPolygon() {
}
function cancelDrawPolygon(){
	drawPolygon.deactivate();
	Square.deactivate();
}
function clearFeatureDrawPolygon(e) {
	wfst.removeAllFeatures();
}
function redrawFeatureDrawPolygon(e){
	var feature=e.feature;
	feature.fid="graph";
	wfst.redraw(feature);
}
function PasteImageTomap(Geomwkt,thumbnailurl,thumbnailname){
	var thumbnailurl = "getImage.htm?filepath="+thumbnailurl+"&name="+thumbnailname;
	var imageoptions = {
			isBaseLayer : false,
			displayInLayerSwitcher : false
	};
	var geom=wkt_c.read(Geomwkt);
	var bounds = geom.geometry.getBounds();
	var size = bounds.getSize();
	var layers=map.getLayersByName(thumbnailname);
	if(layers.length!=0){
		if(layers[0].getVisibility()){
			map.raiseLayer(layers[0],-999);
			layers[0].setVisibility(false);
		}else{
			map.raiseLayer(layers[0],999);
			layers[0].setVisibility(true);
			
		}
	}else{
		var iamgelayer = new OpenLayers.Layer.Image(thumbnailname,
				thumbnailurl, bounds, size, imageoptions);
		iamgelayer.fid="PLLayers";
		map.addLayers([ iamgelayer ]);
	}
}
function PasteImagelayerTomap(Geomwkt,image_url){
	var geom=wkt_c.read(Geomwkt);
	var bounds = geom.geometry.getBounds();
	var layersname=image_url.substring(image_url.lastIndexOf("/")+1,image_url.length);
	var layersServerUrl=image_url.substring(0,image_url.lastIndexOf("/demo"))+"/service/wms";
	var layers=map.getLayersByName(layersname);
	if(layers.length!=0){
		if(layers[0].getVisibility()){
			map.raiseLayer(layers[0],-999);
			layers[0].setVisibility(false);
		}else{
			map.raiseLayer(layers[0],999);
			layers[0].setVisibility(true);
			
		}
	}else{
		var demolayer = new OpenLayers.Layer.WMS(
				layersname,layersServerUrl,
				{
					layers: layersname, 
					format: 'image/png' ,
					transparent : true
					
				},
				{ 
				maxExtent: bounds,
				displayOutsideMaxExtent:false,
				tileSize: new OpenLayers.Size(256,256),
				tileOrigin: new OpenLayers.LonLat(-180.0, 90.0),
				serverResolutions: [0.703125, 0.3515625, 0.17578125, 0.087890625, 0.0439453125, 0.02197265625, 0.010986328125, 0.0054931640625, 0.00274658203125, 0.001373291015625, 6.866455078125E-4, 3.4332275390625E-4, 1.71661376953125E-4, 8.58306884765625E-5, 4.291534423828125E-5, 2.1457672119140625E-5],
				visibility:true
				});
				demolayer.fid="PLLayers";
				map.addLayer(demolayer);
	}
	map.zoomToExtent(bounds);
}
function  ToPastGeomF(Geomwkt,id){
	var geom=wkt_c.read(Geomwkt);
	wfst.removeAllFeatures();
	wfst.addFeatures(geom);
	map.zoomToExtent(geom.geometry.getBounds());
}
function  listToPastGeomF(Geomwkt,id){
	var geom=wkt_c.read(Geomwkt);
	geom.fid=id;
	featurelayer.addFeatures(geom);
	
}
function  listToPastGeomFs(features){
	featurelayer.addFeatures(features);
}
function  ToPastImageGeomF(Geomwkt,id){
	var geom=wkt_c.read(Geomwkt);
	map.zoomToExtent(geom.geometry.getBounds());
}

function  hidelistToPastGeomF2(id){
	var feature=featurelayer.getFeatureByFid(id);
	feature.style={
		display: "none"
	};
   featurelayer.redraw(feature);
		
}
function  showlistToPastGeomF2(id){
	var feature=featurelayer.getFeatureByFid(id);
	feature.style=null;
       	featurelayer.redraw(feature);
		
}
function zoomlistextent(){
	map.zoomToExtent(featurelayer.getDataExtent());
}
function clearfeaturelayer(){
	featurelayer.removeAllFeatures();
}
var geom=0;
function  selectfeaturestyle(id){
	var geom=featurelayer.getFeatureByFid(id);
	selectControl.select(geom);
	/*geom.style={
			strokeColor : "#BB00FF",
			strokeOpacity : 0.3,
			fillColor : "#FFEE00",
			fillOpacity :0.1
		};*/
	//featurelayer.redraw(geom);
	map.zoomToExtent(geom.geometry.getBounds());
}
function  unselectfeaturestyle(id){
	var geom=featurelayer.getFeatureByFid(id);
	selectControl.unselect(geom);
	/*geom.style={
			strokeColor : "#bb0000",
			strokeOpacity : 0.3,
			fillColor : "#993300",
			fillOpacity : 0.1
		};*/
	//featurelayer.redraw(geom);
	map.zoomToExtent(geom.geometry.getBounds());
}
function clearmap(){
	drawPolygon.deactivate();
	Square.deactivate();
	featurelayer.removeAllFeatures();
}

function updateMapMoveend(e) {
	if (!e.object.dragging) { // 缩放
	    var adjustzoom = adjustZoom(map.zoom, map, true);
	    var   chinabounds=new OpenLayers.Bounds(70.40650628256762,-0.9954432050011515,138.18927647840744,56.16093056737958);
	    var   LonLat=chinabounds.getCenterLonLat();
	        if (map.zoom != adjustzoom) {
	            setTimeout(function(){map.setCenter(LonLat,adjustzoom,false,false);},0);
	            return;
	        }
	}
}



/**
* 获取合适的zoom
* 
* @param zoom
*            原zoom
* @param map
*            地图对象
* @param ignoreWrapDateLine
*            忽略日期变更线封装
* @returns
*/
function adjustZoom(zoom, map, ignoreWrapDateLine) {
map = map || g_map;
if (map.baseLayer && (ignoreWrapDateLine || map.baseLayer.wrapDateLine)) {
    var resolutions = map.baseLayer.resolutions, maxExtent = map
            .getMaxExtent(), maxResolution = maxExtent.getWidth()
            / map.size.w;
    if (ignoreWrapDateLine) {
        if (map.fractionalZoom) {
            zoom = map.getZoomForResolution(maxResolution);
        } else {
            for (var i = zoom | 0, ii = resolutions.length; i < ii; ++i) {
                var resolution = resolutions[i];
                if (resolution <= maxResolution) {
                    var extent = map.calculateBounds(map.getCenter(),
                            resolution);
                    if (extent.right > maxExtent.right
                            || extent.left < maxExtent.left
                            || extent.top > maxExtent.top
                            || extent.bottom < maxExtent.bottom) {
                        continue;
                    }
                    zoom = i;
                    break;
                }
            }
        }
    } else if (map.getResolutionForZoom(zoom) > maxResolution) {
        if (map.fractionalZoom) {
            zoom = map.getZoomForResolution(maxResolution);
        } else {
            for (var i = zoom | 0, ii = resolutions.length; i < ii; ++i) {
                if (resolutions[i] <= maxResolution) {
                    zoom = i;
                    break;
                }
            }
        }
    }
}
return zoom;
}
