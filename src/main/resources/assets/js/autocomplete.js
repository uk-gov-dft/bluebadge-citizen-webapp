import accessibleAutocomplete from 'accessible-autocomplete';

export default (options) => {
    const sourceSelect = (query, callback) => {
        const optionsWithAValue = [].filter.call(options.selectElement.options, (option) => {
            return option.value !== '';
        });

        const orgs = optionsWithAValue.map((select) => {

            let dataAbbreviations = select.getAttribute('data-abbreviations');
            dataAbbreviations = dataAbbreviations ? dataAbbreviations.split('|') : [];

            let dataOtherNames = select.getAttribute('data-other-names');
            dataOtherNames = dataOtherNames ? dataOtherNames.split('|') : [];

            return {
                current_name: select.label || select.text,
                abbreviations: dataAbbreviations,
                other_names: dataOtherNames,
            };
        });

        const regexes = query.trim().split(/\s+/).map((word) => {
            const pattern = '\\b' + word.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, '\\$&');
            return new RegExp(pattern, 'i');
        });

        const matches = orgs.map((organisation) => {

            const allNames = [organisation.current_name]
                .concat(organisation.other_names)
                .concat(organisation.abbreviations)
                .filter(name => name);

            organisation.resultPosition = null;

            for (let i = 0; i < allNames.length; i++) {

                const matches = regexes.reduce((acc, regex) => {

                    const matchPosition = allNames[i].search(regex);

                    if (matchPosition > -1) {
                        acc.count += 1;

                        if (acc.lowestPosition === -1 || matchPosition < acc.lowestPosition) {
                            acc.lowestPosition = matchPosition;
                        }
                    }

                    return acc;

                }, { count: 0, lowestPosition: -1 });

                if (matches.count === regexes.length && (organisation['resultPosition'] == null || matches.lowestPosition < organisation['resultPosition'])) {
                    organisation['resultPosition'] = matches.lowestPosition;
                }
            }

            return organisation;

        });

        const filteredMatches = matches.filter((organisation) => {
            return organisation.resultPosition !== null;
        });

        const sortedFilteredMatches = filteredMatches.sort((organisationA, organisationB) => {

            if (organisationA['resultPosition'] < organisationB['resultPosition'] ) {
                return -1;
            } else if (organisationA['resultPosition'] > organisationB['resultPosition'] ) {
                return 1;
            }
            
            return 0;
        });

        const results = sortedFilteredMatches.map(organisation => organisation.current_name);

        return callback(results);
    };

   if (options && !options.source) {
       options.source = sourceSelect;
   }

   if (options && options.selectElement) {
       accessibleAutocomplete.enhanceSelectElement(options);
   }
};
