base([
event('Oxysterols',bind,'LXR'),
event('LXR',activate,'CYP7A1'),
event('CYP7A1',activate,'bile acids'),
event('bile acids',bind,'FXR'),
event('FXR',activate,'SHP'),
event('SHP',bind,'LRH'),
event('LRH',bind,'CYP7A1'),
event('SHP',inhibit,'CYP7A1'),
event('LRH',bind,'SHP'),
event('SHP',inhibit,'SHP')
]).
