'use strict';

const fs = require('fs');

let rawdata = fs.readFileSync('C:\\Users\\chara\\OneDrive\\Documents\\Isirelu\\posts_comments_15022021.json');
var posts = JSON.parse(rawdata);
console.log(posts.length)
posts.forEach(element => {
    delete element._id
    if (element.post != null) {
        element.post = element.post.replace(/"/g, "")
        element.post = element.post.replace(/\(/g, "")
        element.post = element.post.replace(/\)/g, "")
        element.post = element.post.trim()
        var startingChars = ["#", "'", "*", ",", "-", ".", "/", "@", "?", "!", "\n", ":", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"]
        while (element.post != null && element.post.length >0 && startingChars.includes(element.post[0])) {
            if (element.post.length == 1) {
                element.post = ""
                break;
            }
            element.post = element.post.substring(1)
            element.post = element.post.trim();
        }
        
    }
    return element;
});
posts = posts.filter(function(value, index, arr){ 
    if (value.post == null || value.post.length == 0) {
        return false;
    }
    return true;
})
posts = posts.sort(((a, b) => (a.post > b.post) ? 1 : -1))
fs.writeFileSync('C:\\Users\\chara\\OneDrive\\Documents\\Isirelu\\posts_comments_sorted_15022021.json', JSON.stringify(posts));
