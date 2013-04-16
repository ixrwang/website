(function (jQuery) {
    jQuery.fn.page = function(options) {
        options = jQuery.extend({
            url: window.location + "",
            page: 1, size: 20, total: 1, tail: 3
        }, options);
        var ul = jQuery('<ul>');
        var url = options.url + (options.url.indexOf('?') > -1 ? "&" : "?");
        var begin = options.page - options.tail > 1 ? options.page +  options.tail > options.total ? options.total - options.tail * 2 : options.page - options.tail : 1;
        var end = begin + options.tail * 2 + 1;
        ul.append('<li><a href="'+url+'page=1&size='+options.size+'">&laquo;</a></li>');
        for(var i = begin ; i < end && i <= options.total; i ++) {
            if(i == options.page) {
                ul.append('<li class="active"><a>'+i+'</a></li>');
            } else {
                ul.append('<li><a href="'+url+'page='+i+'&size='+options.size+'">'+i+'</a></li>');
            }
        }
        ul.append('<li><a href="'+url+'page='+options.total+'&size='+options.size+'">&raquo;</a></li>');
        jQuery(this).append(ul);
    };
})(window.jQuery);