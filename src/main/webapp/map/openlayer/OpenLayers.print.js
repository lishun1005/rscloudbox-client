OpenLayers.Layer.WMS.Print = OpenLayers.Class(OpenLayers.Layer.WMS, {
	/**
     * Method: getURL
     * Return a GetMap query string for this layer
     *
     * Parameters:
     * bounds - {<OpenLayers.Bounds>} A bounds representing the bbox for the
     *                                request.
     *
     * Returns:
     * {String} A string with the layer's url and parameters and also the
     *          passed-in bounds and appropriate tile size specified as 
     *          parameters.
     */
    getURL: function (bounds) {
        bounds = this.adjustBounds(bounds);
        
        var imageSize = this.getImageSize();
        var newParams = {};
        // WMS 1.3 introduced axis order
        var reverseAxisOrder = this.reverseAxisOrder();
        newParams.BBOX = this.encodeBBOX ?
            bounds.toBBOX(null, reverseAxisOrder) :
            bounds.toArray(reverseAxisOrder);
        newParams.WIDTH = imageSize.w;
        newParams.HEIGHT = imageSize.h;
        var requestString = this.getFullRequestString(newParams);
//        console.log('debug-getURL:'+requestString);
        return requestString;
    },
    
    
	/**
     * Method: initGriddedTiles
     * 
     * Parameters:
     * bounds - {<OpenLayers.Bounds>}
     */
    initGriddedTilesPrint:function(bounds,resolution) {
        this.events.triggerEvent("retile");
        
        // work out mininum number of rows and columns; this is the number of
        // tiles required to cover the viewport plus at least one for panning

        var viewSize = this.map.getSize();
        viewSize.w = (bounds.right-bounds.left)/resolution;
        viewSize.h = (bounds.top-bounds.bottom)/resolution;
        console.log('debug-viewSize:'+viewSize);
        
        var origin = this.getTileOrigin();
        
//        var resolution = this.map.getResolution();
        var serverResolution = resolution;//this.getServerResolution();
        var ratio = resolution / serverResolution;
        var tileSize = {
                w: this.tileSize.w / ratio,
                h: this.tileSize.h / ratio
            };
        
        var minRows = Math.ceil(viewSize.h/tileSize.h) + 
                      2 * this.buffer + 1;
        var minCols = Math.ceil(viewSize.w/tileSize.w) +
                      2 * this.buffer + 1;
//        minRows = Math.ceil(viewSize.h/tileSize.h);
//        minCols = Math.ceil(viewSize.w/tileSize.w);
        console.log('debug-minRows:'+minRows+',minCols:'+minCols);

        var tileLayout = this.calculateGridLayout(bounds, origin, serverResolution);
        this.gridLayout = tileLayout;
        
        var tilelon = tileLayout.tilelon;
        var tilelat = tileLayout.tilelat;
        
        var layerContainerDivLeft = this.map.layerContainerOriginPx.x;
        var layerContainerDivTop = this.map.layerContainerOriginPx.y;

        var tileBounds = this.getTileBoundsForGridIndex(0, 0);
        var startPx = this.map.getViewPortPxFromLonLat(
            new OpenLayers.LonLat(tileBounds.left, tileBounds.top)
        );
        startPx.x = Math.round(startPx.x) - layerContainerDivLeft;
        startPx.y = Math.round(startPx.y) - layerContainerDivTop;

        var tileData = [], center = this.map.getCenter();

        var rowidx = 0;
        do {
            var row = this.grid[rowidx];
            if (!row) {
                row = [];
                this.grid.push(row);
            }
            
            var colidx = 0;
            do {
                tileBounds = this.getTileBoundsForGridIndex(rowidx, colidx);
                var px = startPx.clone();
                px.x = px.x + colidx * Math.round(tileSize.w);
                px.y = px.y + rowidx * Math.round(tileSize.h);
                var tile = row[colidx];
                if (!tile) {
                    tile = this.addTile(tileBounds, px);
                    this.addTileMonitoringHooks(tile);
                    row.push(tile);
                } else {
                    tile.moveTo(tileBounds, px, false);
                }
                var tileCenter = tileBounds.getCenterLonLat();
                tileData.push({
                    tile: tile,
                    distance: Math.pow(tileCenter.lon - center.lon, 2) +
                        Math.pow(tileCenter.lat - center.lat, 2)
                });
     
                colidx += 1;
            } while ((tileBounds.right <= bounds.right + tilelon * this.buffer)
                     || colidx < minCols);
             
            rowidx += 1;
        } while((tileBounds.bottom >= bounds.bottom - tilelat * this.buffer)
                || rowidx < minRows);
        
        //shave off exceess rows and colums
        this.removeExcessTiles(rowidx, colidx);

        /*var resolution = this.getServerResolution();
        // store the resolution of the grid
        this.gridResolution = resolution;

        //now actually draw the tiles
        tileData.sort(function(a, b) {
            return a.distance - b.distance;
        });*/
        
        console.log('debug-tileData:'+tileData.length);
        for (var i=0, ii=tileData.length; i<ii; ++i) {
            tileData[i].tile.draw();
//            console.log('debug-url:'+tileData[i].tile.url);
//            console.log('debug-bounds-left:'+tileData[i].tile.bounds.left);
//            console.log('debug-bounds-bottom:'+tileData[i].tile.bounds.bottom);
//            console.log('debug-bounds-right:'+tileData[i].tile.bounds.right);
//            console.log('debug-bounds-top:'+tileData[i].tile.bounds.top);
//            console.log('debug-position-x:'+tileData[i].tile.position.x);
//            console.log('debug-position-y:'+tileData[i].tile.position.y);
        }
        return tileData;
    },
	
	printSort:function(sort) {
		var x1=sort[0].tile.position.x,x2,x3;
		var xNum=0, yNum=0;
		for(var i=0, ii=sort.length; i<ii; ++i) {
			if(sort[0].tile.position.y == sort[i].tile.position.y) {
				yNum++;
			}
			if(x1 == sort[i].tile.position.x) {
				xNum++;
			} else {
				x2 = sort[i].tile.position.x;
			}
		}
		console.log('sort-x:'+xNum+',y:'+yNum);
		
		var images = new Array();
		for(var i=0; i<sort.length; i++) {
			images[i] = sort[i].tile.url;
		}
	},
	
	CLASS_NAME: "OpenLayers.Layer.WMS.Print"
});