const path = require('path');
const CopyPlugin = require("copy-webpack-plugin");
const Dotenv = require('dotenv-webpack');

// Get the name of the appropriate environment variable (`.env`) file for this build/run of the app
const dotenvFile = process.env.API_LOCATION ? `.env.${process.env.API_LOCATION}` : '.env';

module.exports = {
  plugins: [
    new CopyPlugin({
      patterns: [
        {
          from: "static_assets", to: "../",
          globOptions: {
            ignore: ["**/.DS_Store"],
          },
        },
      ],
    }),
    new Dotenv({ path: dotenvFile }),
  ],
  optimization: {
    usedExports: true
  },
  entry: {
    createHabitat: path.resolve(__dirname, 'src', 'pages', 'createHabitat.js'),
    viewHabitat: path.resolve(__dirname, 'src', 'pages', 'viewHabitat.js'),
    viewUserHabitats: path.resolve(__dirname, 'src', 'pages', 'viewUserHabitats.js'),
    updateHabitat: path.resolve(__dirname, 'src', 'pages', 'updateHabitat.js'),
    viewAnimals: path.resolve(__dirname, 'src', 'pages', 'viewAnimals.js'),
    viewAllHabitats: path.resolve(__dirname, 'src', 'pages', 'ViewAllHabitats.js'),
    viewHabitatEnrichments: path.resolve(__dirname, 'src', 'pages', 'viewHabitatEnrichments.js'),
    updateHabitatEnrichment: path.resolve(__dirname, 'src', 'pages', 'updateHabitatEnrichment.js'),
    viewEnrichmentActivity: path.resolve(__dirname, 'src', 'pages', 'viewEnrichmentActivity.js'),
    viewAllEnrichmentActivities: path.resolve(__dirname, 'src', 'pages', 'viewAllEnrichmentActivities.js'),
    searchEnrichmentActivities: path.resolve(__dirname, 'src', 'pages', 'SearchEnrichmentActivities.js'),
    searchEnrichments: path.resolve(__dirname, 'src', 'pages', 'searchEnrichments.js'),
    viewAcceptableIds: path.resolve(__dirname, 'src', 'pages', 'viewAcceptableIds.js'),
    IndexInitialize: path.resolve(__dirname, 'src', 'pages', 'IndexInitialize.js'),
    viewAnimal: path.resolve(__dirname, 'src', 'pages', 'viewAnimal.js'),
    searchHabitats: path.resolve(__dirname, 'src', 'pages', 'searchHabitats.js')
  },
  output: {
    path: path.resolve(__dirname, 'build', 'assets'),
    filename: '[name].js',
    publicPath: '/assets/'
  },
  devServer: {
    static: {
      directory: path.join(__dirname, 'static_assets'),
    },
    port: 8000,
    client: {
      // overlay shows a full-screen overlay in the browser when there are js compiler errors or warnings
      overlay: true,
    },
  }
}